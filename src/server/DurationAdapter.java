package server;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;


public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long seconds = jsonObject.get("seconds").getAsLong();
        int nanos = jsonObject.get("nanos").getAsInt();
        return Duration.ofSeconds(seconds, nanos);
    }

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("seconds", src.getSeconds());
        jsonObject.addProperty("nanos", src.getNano());
        return jsonObject;
    }
}
