package service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import common.Currency;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

public class CreateAccountServiceRequest {
    private Long customerId;
    private String country;
    private List<Currency> currencies;

    @JsonCreator
    protected CreateAccountServiceRequest() {
    }

    public CreateAccountServiceRequest(Long customerId,
                                       String country,
                                       List<Currency> currencies) {
        this.customerId = customerId;
        this.country = country;
        this.currencies = isNull(currencies) ? Collections.emptyList() : currencies;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCountry() {
        return country;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
