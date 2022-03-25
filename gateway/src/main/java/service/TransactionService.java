package service;

import api.dto.create.transaction.CreateTransactionRequestData;
import api.dto.create.transaction.CreateTransactionResponseData;
import api.dto.get.transactions.GetTransactionsRequestData;
import api.dto.get.transactions.GetTransactionsResponseData;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Service
public class TransactionService {
    private final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final AccountDbService accountDbService;
    private final TaskPublisher taskPublisher;

    @Autowired
    public TransactionService(
            AccountDbService accountDbService,
            TaskPublisher taskPublisher

    ) {
        this.accountDbService = accountDbService;
        this.taskPublisher = taskPublisher;
    }


    public GetTransactionsResponseData findTransactions(@NotNull GetTransactionsRequestData rqData) {
        List<model.entity.Transaction> transactionsOfAccount = accountDbService.findTransactionsOfAccount(rqData.getAccountId());

        return new GetTransactionsResponseData(
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
        );
    }

    public Optional<CreateTransactionResponseData> createTransaction(@NotNull CreateTransactionRequestData rqData) {
        //todo validate
        CreateTransactionServiceRequest serviceRequest = new CreateTransactionServiceRequest(
                rqData.getAccountId(),
                rqData.getAmount(),
                rqData.getCurrency(),
                rqData.getDirection(),
                rqData.getDescription()
        );

        CreateTransactionPayload payload = taskPublisher.publish(RabbitClientConfig.RPC_CREATE_TRANSACTION_MSG_QUEUE, serviceRequest, CreateTransactionPayload.class);
        if (!payload.isSuccess()) {
            return Optional.empty(); //todo replace
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
        );
    }
}
