package service.dto;

import model.entity.Account;
import model.entity.Balance;

import java.util.List;

public class AccountWithBalances {
    private Account account;
    private List<Balance> balances;

    public AccountWithBalances() {
    }

    public AccountWithBalances(Account account, List<Balance> balances) {
        this.account = account;
        this.balances = balances;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public Account getAccount() {
        return account;
    }

    public List<Balance> getBalances() {
        return balances;
    }
}
