package cn.lance.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();

        // feature serialize
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // feature deserialize
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        // Date format
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(dateFormat));

        // JDK 8 date format
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern(dateFormat)
        );
        LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern(dateFormat)
        );
        javaTimeModule.addSerializer(serializer);
        javaTimeModule.addDeserializer(LocalDateTime.class, deserializer);
        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    private JsonUtils() {
    }

    public static ObjectMapper getDefaultObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static ObjectMapper getNewObjectMapper() {
        return OBJECT_MAPPER.copy();
    }

    public static boolean isValidJson(String json) {
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static String write(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static String writePretty(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static String writeWithFeature(
            Object obj,
            List<SerializationFeature> enable,
            List<SerializationFeature> disable
    ) throws JsonProcessingException {
        ObjectMapper currentMapper = getNewObjectMapper();

        if (enable != null) {
            enable.forEach(currentMapper::enable);
        }
        if (disable != null) {
            disable.forEach(currentMapper::disable);
        }

        return currentMapper.writeValueAsString(obj);
    }

    public static <T> T read(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T readWithFeature(
            String json,
            Class<T> clazz,
            List<DeserializationFeature> enable,
            List<DeserializationFeature> disable
    ) throws JsonProcessingException {
        ObjectMapper currentMapper = getNewObjectMapper();
        if (enable != null) {
            enable.forEach(currentMapper::enable);
        }
        if (disable != null) {
            disable.forEach(currentMapper::disable);
        }
        return currentMapper.readValue(json, clazz);
    }

    public static <T> List<T> readList(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readerForListOf(clazz).readValue(json);
    }

    public static Map<String, Object> readMap(String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
        });
    }

    public static JsonNode readTree(String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(json);
    }

    public static String compress(String json) throws JsonProcessingException {
        JsonNode node = OBJECT_MAPPER.readTree(json);
        return OBJECT_MAPPER.writeValueAsString(node);
    }

    public static String pretty(String json) throws JsonProcessingException {
        JsonNode node = OBJECT_MAPPER.readTree(json);
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

}
