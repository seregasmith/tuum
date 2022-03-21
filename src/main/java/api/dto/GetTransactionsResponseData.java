package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class GetTransactionsResponseData {
    private final List<Transaction> transactions;

    @JsonCreator
    public GetTransactionsResponseData(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
