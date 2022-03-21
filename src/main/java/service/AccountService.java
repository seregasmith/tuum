package service;

import api.dto.Balance;
import api.dto.GetAccountRequestData;
import api.dto.GetAccountResponseData;
import model.entity.Account;
import model.service.AccountDbService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.AccountWithBalances;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountService {
    private final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountDbService accountDbService;

    @Autowired
    public AccountService(AccountDbService accountDbService) {
        this.accountDbService = accountDbService;
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
        return Optional.of(new GetAccountResponseData(
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
}
