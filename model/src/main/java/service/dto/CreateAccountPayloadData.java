package service.dto;

public class CreateAccountPayloadData {
    private AccountWithBalances accountWithBalances;

    public CreateAccountPayloadData() {
    }

    public CreateAccountPayloadData(
            AccountWithBalances accountWithBalances) {
        this.accountWithBalances = accountWithBalances;
    }

    public void setAccountWithBalances(AccountWithBalances accountWithBalances) {
        this.accountWithBalances = accountWithBalances;
    }

    public AccountWithBalances getAccountWithBalances() {
        return accountWithBalances;
    }
}
