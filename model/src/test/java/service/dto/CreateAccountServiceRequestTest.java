package service.dto;

import common.Currency;
import common.JsonSerializer;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CreateAccountServiceRequestTest {
    @Test
    public void serializeDeserializeTest() {
        CreateAccountServiceRequest rq = new CreateAccountServiceRequest(1L, "Cu", Arrays.asList(Currency.GBP));
        String str = JsonSerializer.toJson(rq);
        CreateAccountServiceRequest after = JsonSerializer.parseJson(str, CreateAccountServiceRequest.class);

        assertEquals(rq.getCountry(), after.getCountry());
        assertEquals(rq.getCustomerId(), after.getCustomerId());
        assertEquals(rq.getCurrencies().size(), after.getCurrencies().size());
        assertEquals(rq.getCurrencies().get(0), after.getCurrencies().get(0));
    }
}