package api.validate;

import service.dto.ErrorPayload;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ValidationResult {
    private ErrorPayload error;

    private ValidationResult() {
    }

    public static ValidationResult asSuccess() {
        return new ValidationResult();
    }

    public static ValidationResult asError(ErrorPayload error) {
        ValidationResult res = new ValidationResult();
        res.error = error;
        return res;
    }

    public Boolean isSuccess() {
        return isNull(error);
    }

    public Boolean hasError() {
        return nonNull(error);
    }

    public ErrorPayload getError() {
        return error;
    }
}
