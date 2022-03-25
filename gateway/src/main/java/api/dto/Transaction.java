package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import common.Currency;
import common.Direction;

import java.math.BigDecimal;

public class Transaction {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    private String description;

    @JsonCreator
    private Transaction() {
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

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.accountId = this.accountId;
            transaction.transactionId = this.transactionId;
            transaction.amount = this.amount;
            transaction.currency = this.currency;
            transaction.direction = this.direction;
            transaction.description = this.description;
            return transaction;
        }
    }
}
