package api.validate;

import api.dto.create.transaction.CreateTransactionRequestData;
import org.springframework.stereotype.Service;
import service.dto.ErrorPayload;

import java.math.BigDecimal;

import static common.ErrorCode.EMPTY_PARAMS;
import static common.ErrorCode.INCORRECT_PARAMS;
import static java.util.Objects.isNull;

@Service
public class CreateTransactionValidator implements Validator<CreateTransactionRequestData> {
    @Override
    public ValidationResult validate(CreateTransactionRequestData data) {
        if (isNull(data)) {
            return ValidationResult.asError(
                    new ErrorPayload(EMPTY_PARAMS, "Incoming data is NULL")
            );
        }
        if (isNull(data.getAccountId())) {
            return ValidationResult.asError(
                    new ErrorPayload(EMPTY_PARAMS, "Account id is NULL")
            );
        }
        if (isNull(data.getCurrency())) {
            return ValidationResult.asError(
                    new ErrorPayload(INCORRECT_PARAMS, "Not supported currency")
            );
        }
        if (isNull(data.getDirection())) {
            return ValidationResult.asError(
                    new ErrorPayload(INCORRECT_PARAMS, "Direction should be IN or OUT")
            );
        }
        if (isNull(data.getAmount())) {
            return ValidationResult.asError(
                    new ErrorPayload(EMPTY_PARAMS, "Amount is NULL")
            );
        }
        if (data.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.asError(
                    new ErrorPayload(INCORRECT_PARAMS, "Amount should be positive")
            );
        }

        return ValidationResult.asSuccess();
    }
}
