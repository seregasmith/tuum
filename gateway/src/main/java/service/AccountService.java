package service;

import api.dto.common.Balance;
import api.dto.create.account.CreateAccountRequestData;
import api.dto.create.account.CreateAccountResponseData;
import api.dto.get.account.GetAccountRequestData;
import api.dto.get.account.GetAccountResponseData;
import model.entity.Account;
import model.service.AccountDbService;
import mq.create.RabbitClientConfig;
import mq.create.TaskPublisher;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.AccountWithBalances;
import service.dto.CreateAccountPayload;
import service.dto.CreateAccountPayloadData;
import service.dto.CreateAccountServiceRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Optional.of;

@Service
public class AccountService {
    private final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountDbService accountDbService;
    private final TaskPublisher taskPublisher;

    @Autowired
    public AccountService(AccountDbService accountDbService,
                          TaskPublisher taskPublisher) {
        this.accountDbService = accountDbService;
        this.taskPublisher = taskPublisher;
    }

    public Optional<GetAccountResponseData> findAccount(@NotNull GetAccountRequestData rqData) {
        Optional<AccountWithBalances> accountOpt = accountDbService.findAccountWithBalances(rqData.getAccountId());
        if (accountOpt.isEmpty()) {
            LOG.error("Not found for id={}", rqData.getAccountId());
            return Optional.empty();
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
        );
    }

    public Optional<CreateAccountResponseData> createAccount(@NotNull CreateAccountRequestData rqData) {
        CreateAccountServiceRequest serviceRequest =
                new CreateAccountServiceRequest(rqData.getCustomerId(), rqData.getCountry(), rqData.getCurrencies());
        CreateAccountPayload payload = taskPublisher.publish(
                RabbitClientConfig.RPC_CREATE_ACCOUNT_MSG_QUEUE,
                serviceRequest,
                CreateAccountPayload.class
        );

        CreateAccountPayloadData payloadData = payload.getData();
        if (!payload.isSuccess() || isNull(payloadData)) {
            //todo handle error
            return Optional.empty();
        }
        return of(payloadData)
                .map(CreateAccountPayloadData::getAccountWithBalances)
                .map(CreateAccountResponseData::new);
    }
}
