package vn.backend.ksv.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import vn.backend.ksv.common.exception.CommonException;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:38 AM
 */
public class JsonConverter {

    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
            .create();
    private static JsonParser parser = new JsonParser();

    public static Gson getGson() {

        if (gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().create();
        }
        return gson;
    }

    public static JsonParser getParser() {
        if (parser == null) {
            parser = new JsonParser();
        }
        return parser;
    }

    public static JsonElement toJsonElement(Object object) {
        return getGson().toJsonTree(object);
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static JsonElement toJsonElement(String json) {
        return getParser().parse(json);
    }

    public static com.google.gson.JsonObject toJson(String json) {
//        return gson.fromJson(json, com.google.gson.JsonObject.class);
        return getParser().parse(json).getAsJsonObject();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return getGson().fromJson(json, type);
    }

    public static <T> T fromJson(com.google.gson.JsonObject json, Type type) {
        return getGson().fromJson(json, type);
    }

    public static <T> T fromJson(JsonObject jsonObject, Class<T> clazz) {
        return Json.decodeValue(jsonObject.encode(), clazz);
    }

    public static <T> T fromJson(com.google.gson.JsonObject jsonObject, Class<T> clazz) {
        return getGson().fromJson(jsonObject, clazz);
    }

    public static <T> T fromJson(JsonElement jsonObject, Class<T> clazz) {
        return getGson().fromJson(jsonObject, clazz);
    }

    public static <T> T fromJsonAcceptSingleValue(String json, Class<T> clazz) throws JsonProcessingException {
        return new ObjectMapper()
                .reader(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .forType(clazz)
                .readValue(json);
    }

    public static <T> List<T> fromJsonToList(JsonElement jsonElement, Class<T[]> clazz) throws CommonException.UnsupportException {
        T[] array = new Gson().fromJson(jsonElement, clazz);
        return Arrays.asList(array);
    }

    public static <T> List<T> fromJsonToList(String jsonString, Class<T[]> clazz) throws CommonException.UnsupportException {
        T[] array = new Gson().fromJson(jsonString, clazz);
        return Arrays.asList(array);
    }

    public static <T> Set<T> fromJsonToSet(String jsonString, Class<T[]> clazz) throws CommonException.UnsupportException {
        T[] array = new Gson().fromJson(jsonString, clazz);
        return new HashSet<>(Arrays.asList(array));
    }

    public static String toString(JsonElement json) {
        return getGson().toJson(json);
    }

    public static com.google.gson.JsonObject fromMultiMap(MultiMap map) throws CommonException.ValidationError {
        com.google.gson.JsonObject result = new com.google.gson.JsonObject();
        if (map == null) {
            throw new CommonException.ValidationError("Multi map is null");
        }
        for (Map.Entry<String, String> entry : map.entries()) {
            if (map.getAll(entry.getKey()).size() > 1) {
                if (result.has(entry.getKey())) {
                    result.get(entry.getKey()).getAsJsonArray().add(entry.getValue());
                } else {
                    JsonArray array = new JsonArray();
                    array.add(entry.getValue());
                    result.add(entry.getKey(), array);
                }
            } else {
                result.addProperty(entry.getKey(), entry.getValue());
            }

        }
        return result;
    }

    public static Map<String, JsonElement> toMultiMap(String json) {
        Type type = new TypeToken<Map<String, JsonElement>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static Map toMultiMap(Object json) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(json, Map.class);
    }

    /**
     * Add all element of json_two into json_one
     *
     * @param json_one
     * @param json_two
     * @return
     */
    public static com.google.gson.JsonObject addJsonToJson(com.google.gson.JsonObject json_one, com.google.gson.JsonObject json_two) {
        for (Map.Entry<String, JsonElement> element : json_two.entrySet()) {
            json_one.add(element.getKey(), element.getValue());
        }
        return json_one;
    }

    /**
     * Add all element of json_two into json_one
     *
     * @param json_one
     * @param json_two
     * @return
     */
    public static String addJsonToJson(JsonObject json_one, com.google.gson.JsonObject json_two) {
        com.google.gson.JsonObject temp = toJson(json_one.encode());
        for (Map.Entry<String, JsonElement> element : json_two.entrySet()) {
            temp.add(element.getKey(), element.getValue().getAsJsonPrimitive());
        }
        return temp.toString();
    }


    //todo implement function queryDsl to json
//    @SuppressWarnings({"all"})
//    public static String recordToJson(Record record) {
//        HashMap h = new HashMap();
//        for (Field f : record.fields()) {
//            if (record.get(f) == null) {
//                continue;
//            }
//            if (f.getType().equals(Timestamp.class)) {
//                h.put(f.getName(), ((Timestamp) record.get(f)).getTime());
//                continue;
//            }
//            if (f.getType().equals(Date.class)) {
//                h.put(f.getName(), ((Date) record.get(f)).getTime());
//                continue;
//            }
//            if (f.getType().equals(java.sql.Date.class)) {
//                h.put(f.getName(), ((java.sql.Date) record.get(f)).getTime());
//                continue;
//            }
//            h.put(f.getName(), record.get(f));
//        }
//        return toJson(h);
//    }

    //use for generic class
    @SuppressWarnings({"all"})
    public static <OUT,IN> OUT fromJson(String json,Class<OUT> out,Class<IN> in){
        //Example: ABC<DEF> -> OUT = ABC.class and IN = DEF.class
        Type type = TypeToken.getParameterized(out,in).getType();
        return (OUT)(getGson().fromJson(json,type));
    }

    @SuppressWarnings({"all"})
    public static <OUT,IN> OUT fromJson(JsonElement json,Class<OUT> out,Class<IN> in){
        Type type = TypeToken.getParameterized(out,in).getType();
        return (OUT)(new Gson().fromJson(json,type));
    }

    public static String fromMap(Map<String, Object> map) {
        return new Gson().toJson(map);
    }

    public static JsonElement fromMapToJson(Map<String, Object> map) {
        return new Gson().toJsonTree(map);
    }

    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.getDecoder().decode(json.getAsString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }
    }

    public static <OUT> OUT mapToObject(Object object, Class<OUT> out) {
        return gson.fromJson(getGson().toJson(object), out);
    }
}
