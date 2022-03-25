package service.dto;

import common.Currency;
import common.Direction;

import java.math.BigDecimal;

public class CreateTransactionServiceRequest {
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    private String description;

    public CreateTransactionServiceRequest() {
    }

    public CreateTransactionServiceRequest(
            Long accountId,
            BigDecimal amount,
            Currency currency,
            Direction direction,
            String description
    ) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.direction = direction;
        this.description = description;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getDescription() {
        return description;
    }
}
