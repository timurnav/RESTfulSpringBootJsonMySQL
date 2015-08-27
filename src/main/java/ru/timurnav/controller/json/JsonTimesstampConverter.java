package ru.timurnav.controller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class JsonTimesstampConverter {
    public static class UserSettingSerializer extends JsonSerializer<Timestamp> {
        @Override
        public void serialize(Timestamp value,
                              JsonGenerator gen,
                              SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeString(value.getTime() + "");
        }

        @Override
        public Class<Timestamp> handledType() {
            return Timestamp.class;
        }
    }

    public static class UserSettingDeserializer extends JsonDeserializer<Timestamp> {

        @Override
        public Timestamp deserialize(JsonParser p,
                                     DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new Timestamp(p.getLongValue());
        }

        @Override
        public Class<Timestamp> handledType() {
            return Timestamp.class;
        }
    }
}
