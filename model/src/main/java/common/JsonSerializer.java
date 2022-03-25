package common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import static java.util.Optional.ofNullable;

public class JsonSerializer {
    private static final Logger LOG = LoggerFactory.getLogger(JsonSerializer.class);

    private JsonSerializer() { //static methods only
    }

    private static final ObjectMapper OBJECT_MAPPER = initObjectMapper();

    private static ObjectMapper initObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                .configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
                .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
                .setFilterProvider(new SimpleFilterProvider());
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        if (ObjectUtils.isEmpty(data)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.treeToValue(OBJECT_MAPPER.readTree(data), clazz);
        } catch (JsonProcessingException e) {
            LOG.warn("Exception during parsing json", e);
            return null;
        }
    }

    public static String toJson(Object object) {
        return ofNullable(toJsonNode(object))
                .map(JsonNode::toString)
                .orElse(null);
    }

    public static JsonNode toJsonNode(Object object) {
        return OBJECT_MAPPER.valueToTree(object);
    }
}
