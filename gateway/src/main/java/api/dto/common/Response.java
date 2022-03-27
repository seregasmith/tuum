package api.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import service.dto.ErrorPayload;

import static java.util.Objects.isNull;

public class Response {
    private Object data;
    private ErrorPayload error;

    @JsonCreator
    private Response() {
    }

    public static Response asSuccess(Object data) {
        Response res = new Response();
        res.data = data;
        return res;
    }

    public static Response asError(ErrorPayload error) {
        Response res = new Response();
        res.error = error;
        return res;
    }

    public Object getData() {
        return data;
    }

    public ErrorPayload getError() {
        return error;
    }

    @JsonIgnore
    public Boolean hasError() {
        return isNull(error);
    }
}
