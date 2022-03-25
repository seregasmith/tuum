package service.dto;

import common.ErrorCode;
import common.JsonSerializer;
import model.entity.Account;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class CreateAccountPayloadTest {

    @Test
    public void serializeDeserializeTest() {
        CreateAccountPayload payload = new CreateAccountPayload(
                new CreateAccountPayloadData(new AccountWithBalances(new Account(), Collections.emptyList()))
        );
        String str = JsonSerializer.toJson(payload);
        CreateAccountPayload deserialized = JsonSerializer.parseJson(str, CreateAccountPayload.class);

        //region ASSERTIONS
        assertNotNull(deserialized);
        CreateAccountPayloadData deserializedPayloadData = deserialized.getData();
        assertNotNull(deserializedPayloadData);
        AccountWithBalances accountWBBefore = payload.getData().getAccountWithBalances();
        AccountWithBalances accountWBAfter = deserializedPayloadData.getAccountWithBalances();

        Account accountBefore = accountWBBefore.getAccount();
        Account accountAfter = accountWBAfter.getAccount();
        assertEquals(accountBefore.getId(), accountAfter.getId());
        assertEquals(accountBefore.getCountry(), accountAfter.getCountry());
        assertEquals(accountBefore.getCustomerId(), accountAfter.getCustomerId());

        //endregion
    }

    @Test
    public void serializeDeserializeErrorTest() {
        CreateAccountPayload payload = CreateAccountPayload.asError(ErrorCode.NOT_FOUND, "Some reason");

        String str = JsonSerializer.toJson(payload);

        CreateAccountPayload payloadAfter = JsonSerializer.parseJson(str, CreateAccountPayload.class);

        assertNotNull(payloadAfter);
        assertNull(payloadAfter.getData());
        assertEquals(payload.getError().getCode(), payloadAfter.getError().getCode());
        assertEquals(payload.getError().getReason(), payloadAfter.getError().getReason());
    }
}