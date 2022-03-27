package service;

import api.dto.common.Balance;
import api.dto.common.Response;
import api.dto.create.account.CreateAccountRequestData;
import api.dto.create.account.CreateAccountResponseData;
import api.dto.get.account.GetAccountRequestData;
import api.dto.get.account.GetAccountResponseData;
import api.validate.CreateAccountValidator;
import api.validate.ValidationResult;
import common.ErrorCode;
import model.entity.Account;
import model.service.AccountDbService;
import mq.create.RabbitClientConfig;
import mq.create.TaskPublisher;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.of;

@Service
public class AccountService {
    private static final ErrorPayload INTERNAL_SERVER_ERROR = new ErrorPayload(ErrorCode.UNKNOWN, "Internal server Error");
    private final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountDbService accountDbService;
    private final TaskPublisher taskPublisher;
    private final CreateAccountValidator validator;

    @Autowired
    public AccountService(AccountDbService accountDbService,
                          TaskPublisher taskPublisher,
                          CreateAccountValidator createAccountValidator) {
        this.accountDbService = accountDbService;
        this.taskPublisher = taskPublisher;
        this.validator = createAccountValidator;
    }

    public Response findAccount(@NotNull GetAccountRequestData rqData) {
        Optional<AccountWithBalances> accountOpt = accountDbService.findAccountWithBalances(rqData.getAccountId());
        if (accountOpt.isEmpty()) {
            LOG.error("Not found for id={}", rqData.getAccountId());
            return Response.asError(new ErrorPayload(ErrorCode.NOT_FOUND, "Account not found"));
        }
        Account account = accountOpt
                .map(AccountWithBalances::getAccount)
                .orElseThrow(() -> new RuntimeException("It's not supposed to be"));
        return of(new GetAccountResponseData(
                        account.getId(),
                        account.getCustomerId(),
                        accountOpt
                                .map(AccountWithBalances::getBalances)
                                .map(Collection::stream)
                                .orElse(Stream.empty())
                                .map(b -> new Balance(b.getAmount(), b.getCurrency()))
                                .collect(Collectors.toList())
                )
        ).map(Response::asSuccess)
                .orElse(Response.asError(INTERNAL_SERVER_ERROR));
    }

    public Response createAccount(@NotNull CreateAccountRequestData rqData) {
        ValidationResult validationResult = validator.validate(rqData);
        if (validationResult.hasError()) {
            Response.asError(validationResult.getError());
        }
        CreateAccountServiceRequest serviceRequest =
                new CreateAccountServiceRequest(rqData.getCustomerId(), rqData.getCountry(), rqData.getCurrencies());
        CreateAccountPayload payload = taskPublisher.publish(
                RabbitClientConfig.RPC_CREATE_ACCOUNT_MSG_QUEUE,
                serviceRequest,
                CreateAccountPayload.class
        );

        CreateAccountPayloadData payloadData = payload.getData();
        if (!payload.isSuccess()) {
            return Response.asError(payload.getError());
        }
        return of(payloadData)
                .map(CreateAccountPayloadData::getAccountWithBalances)
                .map(CreateAccountResponseData::new)
                .map(Response::asSuccess)
                .orElse(Response.asError(
                        INTERNAL_SERVER_ERROR
                ));
    }
}
