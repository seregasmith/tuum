package service.dto;

import common.JsonSerializer;
import model.entity.Balance;
import model.entity.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CreateTransactionPayloadTest {
    @Test
    public void serializeDeserializeTest() {
        CreateTransactionPayload payload = new CreateTransactionPayload(
                new CreateTransactionPayloadData(new Transaction(), new Balance())
        );

        String str = JsonSerializer.toJson(payload);
        CreateTransactionPayload payloadAfter = JsonSerializer.parseJson(str, CreateTransactionPayload.class);

        assertNotNull(payloadAfter.getData().getTransaction());
        assertNotNull(payloadAfter.getData().getBalanceAfterTransaction());
    }
}
