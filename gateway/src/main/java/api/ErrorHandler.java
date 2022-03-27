package api;

import api.dto.common.Response;
import common.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
public class ErrorHandler {
    Map<ErrorCode, HttpStatus> mapping = new EnumMap<>(ErrorCode.class);

    {
        mapping.put(ErrorCode.DB_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        mapping.put(ErrorCode.EMPTY_PARAMS, HttpStatus.BAD_REQUEST);
        mapping.put(ErrorCode.INCORRECT_PARAMS, HttpStatus.BAD_REQUEST);
        mapping.put(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        mapping.put(ErrorCode.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity handleError(@NotNull Response response) {
        HttpStatus httpStatus = ofNullable(response.getError())
                .filter(er -> mapping.containsKey(er.getCode()))
                .map(er -> mapping.get(er.getCode()))
                .orElse(HttpStatus.BAD_GATEWAY);
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
