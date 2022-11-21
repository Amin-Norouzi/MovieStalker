package com.aminnorouzi.ms.annotation;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.DecimalFormat;

public class CustomNumberConverter extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);
        final Double value = node.asDouble();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        Double decimal = Double.valueOf(decimalFormat.format(value));

        return decimal;
    }
}
