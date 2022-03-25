package api.dto.get.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GetAccountRequestData {
    private final Long accountId;

    @JsonCreator
    public GetAccountRequestData(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
