package api.dto.create.account;

import api.dto.common.Balance;
import org.jetbrains.annotations.NotNull;
import service.dto.AccountWithBalances;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateAccountResponseData {
    private Long accountId;
    private Long customerId;
    private List<Balance> balances;

    public CreateAccountResponseData() {
    }

    public CreateAccountResponseData(Long accountId,
                                     Long customerId,
                                     List<Balance> balances) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balances = balances;
    }

    public CreateAccountResponseData(@NotNull AccountWithBalances src) {
        Optional.of(src)
                .map(AccountWithBalances::getAccount)
                .ifPresent(a -> {
                    this.accountId = a.getId();
                    this.customerId = a.getCustomerId();
                });

        this.balances = Optional.of(src)
                .map(AccountWithBalances::getBalances)
                .orElse(Collections.emptyList())
                .stream()
                .map(b -> new Balance(b.getAmount(), b.getCurrency()))
                .collect(Collectors.toList());
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }
}
