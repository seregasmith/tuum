package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import common.Currency;

import java.math.BigDecimal;

public class Balance {
    private final BigDecimal availableAmount;
    private final Currency currency;

    @JsonCreator
    public Balance(BigDecimal availableAmount, Currency currency) {
        this.availableAmount = availableAmount;
        this.currency = currency;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
