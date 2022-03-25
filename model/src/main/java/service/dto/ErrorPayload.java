package service.dto;

import common.ErrorCode;

public class ErrorPayload {
    private final ErrorCode code;
    private final String reason;

    private ErrorPayload() {
        this.code = null;
        this.reason = null;
    }

    public ErrorPayload(ErrorCode code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
