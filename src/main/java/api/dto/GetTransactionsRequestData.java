package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GetTransactionsRequestData {
    private final Long accountId;

    @JsonCreator
    public GetTransactionsRequestData(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
