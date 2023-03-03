package com.aminnorouzi.ms.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CalculatedTimeConverter extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);

        int value;
        try {
            value = node.elements().next().asInt();
        } catch (Exception exception) {
            value = node.asInt();
        }

        StringBuilder builder = new StringBuilder();
        int hours = value / 60;
        int minutes = value % 60;

        if (hours != 0) {
            builder.append(hours).append("h ");
        }
        if (minutes != 0) {
            builder.append(minutes).append("m");
        }

        String runtime = builder.toString();
        if (runtime.isBlank()) {
            runtime = String.valueOf(0);
        }

        return runtime;
    }
}
