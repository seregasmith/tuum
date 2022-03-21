package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class GetAccountResponseData {
    private final Long accountId;
    private final Long customerId;
    private final List<Balance> balances;

    @JsonCreator
    public GetAccountResponseData(Long accountId, Long customerId, List<Balance> balances) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balances = balances;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<Balance> getBalances() {
        return balances;
    }
}
