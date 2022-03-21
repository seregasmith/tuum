package service.dto;

import model.entity.Account;
import model.entity.Balance;

import java.util.List;

public class AccountWithBalances {
    private final Account account;
    private final List<Balance> balances;

    public AccountWithBalances(Account account, List<Balance> balances) {
        this.account = account;
        this.balances = balances;
    }

    public Account getAccount() {
        return account;
    }

    public List<Balance> getBalances() {
        return balances;
    }
}
