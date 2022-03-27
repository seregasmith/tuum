package api.dto.create.transaction;

import api.dto.common.Balance;
import common.Currency;
import common.Direction;

import java.math.BigDecimal;

public class CreateTransactionResponseData {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    private String description;
    private Balance balanceAfterTransaction;

    public CreateTransactionResponseData() {
    }

    //region GETTERS
    public Long getAccountId() {
        return accountId;
    }

    public Long getTransactionId() {
        return transactionId;
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

    public Balance getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
    //endregion

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long accountId;
        private Long transactionId;
        private BigDecimal amount;
        private Currency currency;
        private Direction direction;
        private String description;
        private Balance balanceAfterTransaction;

        public Builder withAccountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withTransactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withBalanceAfterTransaction(Balance balanceAfterTransaction) {
            this.balanceAfterTransaction = balanceAfterTransaction;
            return this;
        }

        public CreateTransactionResponseData build() {
            CreateTransactionResponseData rsData = new CreateTransactionResponseData();
            rsData.accountId = this.accountId;
            rsData.transactionId = this.transactionId;
            rsData.amount = this.amount;
            rsData.currency = this.currency;
            rsData.direction = this.direction;
            rsData.description = this.description;
            rsData.balanceAfterTransaction = this.balanceAfterTransaction;
            return rsData;
        }
    }

}
