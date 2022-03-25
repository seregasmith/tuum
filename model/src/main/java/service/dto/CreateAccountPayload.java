package service.dto;

import common.ErrorCode;

public class CreateAccountPayload extends Payload {
    private CreateAccountPayloadData data;

    public CreateAccountPayload() {
    }

    public CreateAccountPayload(CreateAccountPayloadData data) {
        this.data = data;
        asSuccess();
    }

    public CreateAccountPayloadData getData() {
        return data;
    }

    public static CreateAccountPayload asError(ErrorCode code, String reason) {
        return new CreateAccountPayload().asNotSuccess(code, reason);
    }
}
