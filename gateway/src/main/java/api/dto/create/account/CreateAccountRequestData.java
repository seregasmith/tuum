package api.dto.create.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import common.Currency;

import java.util.List;

public class CreateAccountRequestData {
    private String country;
    private Long customerId;
    private List<Currency> currencies;

    @JsonCreator
    public CreateAccountRequestData(String country, Long customerId, List<Currency> currencies) {
        this.country = country;
        this.customerId = customerId;
        this.currencies = currencies;
    }

    public String getCountry() {
        return country;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
