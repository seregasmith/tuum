package service.dto;

import model.entity.Balance;
import model.entity.Transaction;

public class CreateTransactionPayloadData {
    private Transaction transaction;
    private Balance balanceAfterTransaction;

    public CreateTransactionPayloadData() {
    }

    public CreateTransactionPayloadData(Transaction transaction,
                                        Balance balanceAfterTransaction) {
        this.transaction = transaction;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setBalanceAfterTransaction(Balance balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Balance getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
}
