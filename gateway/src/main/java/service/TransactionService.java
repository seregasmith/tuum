package service;

import api.dto.common.Response;
import api.dto.create.transaction.CreateTransactionRequestData;
import api.dto.create.transaction.CreateTransactionResponseData;
import api.dto.get.transactions.GetTransactionsRequestData;
import api.dto.get.transactions.GetTransactionsResponseData;
import api.validate.CreateTransactionValidator;
import api.validate.ValidationResult;
import common.ErrorCode;
import model.entity.Balance;
import model.entity.Transaction;
import model.service.AccountDbService;
import mq.create.RabbitClientConfig;
import mq.create.TaskPublisher;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.CreateTransactionPayload;
import service.dto.CreateTransactionPayloadData;
import service.dto.CreateTransactionServiceRequest;
import service.dto.ErrorPayload;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Service
public class TransactionService {
    private static final ErrorPayload INTERNAL_SERVER_ERROR = new ErrorPayload(ErrorCode.UNKNOWN, "Internal server Error");
    private final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final AccountDbService accountDbService;
    private final TaskPublisher taskPublisher;
    private final CreateTransactionValidator validator;

    @Autowired
    public TransactionService(
            AccountDbService accountDbService,
            TaskPublisher taskPublisher,
            CreateTransactionValidator validator

    ) {
        this.accountDbService = accountDbService;
        this.taskPublisher = taskPublisher;
        this.validator = validator;
    }


    public Response findTransactions(@NotNull GetTransactionsRequestData rqData) {
        List<model.entity.Transaction> transactionsOfAccount = accountDbService.findTransactionsOfAccount(rqData.getAccountId());

        return of(new GetTransactionsResponseData(
                        transactionsOfAccount
                                .stream()
                                .map(tr -> api.dto.Transaction.builder()
                                        .withTransactionId(tr.getId())
                                        .withAccountId(tr.getAccount().getId())
                                        .withAmount(tr.getAmount())
                                        .withCurrency(tr.getCurrency())
                                        .withDescription(tr.getDescription())
                                        .withDirection(tr.getDirection())
                                        .build()
                                ).collect(Collectors.toList())
                )
        )
                .map(Response::asSuccess)
                .orElse(Response.asError(INTERNAL_SERVER_ERROR))
                ;
    }

    public Response createTransaction(@NotNull CreateTransactionRequestData rqData) {
        ValidationResult validationResult = validator.validate(rqData);
        if (validationResult.hasError()) {
            return Response.asError(validationResult.getError());
        }
        CreateTransactionServiceRequest serviceRequest = new CreateTransactionServiceRequest(
                rqData.getAccountId(),
                rqData.getAmount(),
                rqData.getCurrency(),
                rqData.getDirection(),
                rqData.getDescription()
        );

        CreateTransactionPayload payload = taskPublisher.publish(RabbitClientConfig.RPC_CREATE_TRANSACTION_MSG_QUEUE, serviceRequest, CreateTransactionPayload.class);
        if (!payload.isSuccess()) {
            return Response.asError(payload.getError());
        }

        CreateTransactionPayloadData payloadData = payload.getData();
        Transaction transaction = payloadData.getTransaction();
        Balance balanceAfterTransaction = payloadData.getBalanceAfterTransaction();
        return of(CreateTransactionResponseData
                .builder()
                .withTransactionId(transaction.getId())
                .withAccountId(transaction.getAccount().getId())
                .withAmount(transaction.getAmount())
                .withDescription(transaction.getDescription())
                .withDirection(transaction.getDirection())
                .withCurrency(transaction.getCurrency())
                .withBalanceAfterTransaction(
                        new api.dto.common.Balance(
                                balanceAfterTransaction.getAmount(), balanceAfterTransaction.getCurrency()
                        )
                )
                .build()
        ).map(Response::asSuccess)
                .orElse(Response.asError(
                        INTERNAL_SERVER_ERROR
                ));
    }
}
