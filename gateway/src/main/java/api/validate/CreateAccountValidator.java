package api.validate;

import api.dto.create.account.CreateAccountRequestData;
import common.ErrorCode;
import org.springframework.stereotype.Service;
import service.dto.ErrorPayload;

import static java.util.Objects.isNull;

@Service
public class CreateAccountValidator implements Validator<CreateAccountRequestData> {
    @Override
    public ValidationResult validate(CreateAccountRequestData data) {
        if (isNull(data)) {
            return ValidationResult.asError(
                    new ErrorPayload(ErrorCode.INCORRECT_PARAMS, "Incoming data is NULL")
            );
        }
        if (isNull(data.getCurrencies()) || data.getCurrencies().isEmpty()) {
            return ValidationResult.asError(
                    new ErrorPayload(
                            ErrorCode.INCORRECT_PARAMS,
                            "Should be at least one of currency in request"
                    )
            );
        }
        return ValidationResult.asSuccess();
    }
}
