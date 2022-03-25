package service.dto;

import common.ErrorCode;

public abstract class Payload {
    private Boolean success;
    private ErrorPayload error;

    protected Payload() {
    }

    protected <T extends Payload> T asNotSuccess(ErrorCode code, String reason) {
        this.error = new ErrorPayload(code, reason);
        this.success = false;
        return (T) this;
    }

    protected <T extends Payload> T asSuccess() {
        this.success = true;
        return (T) this;
    }

    public ErrorPayload getError() {
        return error;
    }

    public Boolean isSuccess() {
        return success;
    }
}
