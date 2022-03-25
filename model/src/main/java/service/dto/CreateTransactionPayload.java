package service.dto;

import common.ErrorCode;

public class CreateTransactionPayload extends Payload {
    private CreateTransactionPayloadData data;

    public CreateTransactionPayload() {
    }

    public CreateTransactionPayload(CreateTransactionPayloadData data) {
        this.data = data;
        asSuccess();
    }

    public void setData(CreateTransactionPayloadData data) {
        this.data = data;
    }

    public CreateTransactionPayloadData getData() {
        return data;
    }

    public static CreateTransactionPayload asError(ErrorCode errorCode, String reason) {
        return new CreateTransactionPayload()
                .asNotSuccess(errorCode, reason);
    }
}
