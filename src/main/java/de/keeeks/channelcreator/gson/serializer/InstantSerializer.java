package de.keeeks.channelcreator.gson.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.keeeks.channelcreator.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerializer extends JsonSerializer<Instant> {

    public InstantSerializer() {
        super(Instant.class);
    }

    @Override
    public void write(JsonObject jsonElement, Type type, Instant t, JsonSerializationContext jsonSerializationContext) {
        jsonElement.addProperty("time", t.toEpochMilli());
    }

    @Override
    public Instant read(JsonObject element, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return Instant.ofEpochMilli(element.get("time").getAsLong());
    }
}