package de.keeeks.channelcreator.gson;

import com.google.gson.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

@Getter
@RequiredArgsConstructor
public abstract class JsonSerializer<T> implements com.google.gson.JsonSerializer<T>, JsonDeserializer<T>, PrimitiveResolve {

    private final Class<T> type;

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return read(jsonElement.getAsJsonObject(), type, jsonDeserializationContext);
    }

    @Override
    @SneakyThrows
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        var jsonObject = new JsonObject();
        write(jsonObject, type, t, jsonSerializationContext);
        return jsonObject;
    }

    public abstract void write(JsonObject jsonElement, Type type, T t, JsonSerializationContext jsonSerializationContext);

    public abstract T read(JsonObject element, Type type, JsonDeserializationContext jsonDeserializationContext);

}