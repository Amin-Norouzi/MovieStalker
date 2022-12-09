package com.aminnorouzi.ms.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.text.DecimalFormat;

public class SimpleDoubleNumberConverter extends JsonDeserializer<Double> {

    @Value("${movie.client.api.rating.pattern}")
    private String pattern;

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);
        final Double value = node.asDouble();

        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return Double.valueOf(decimalFormat.format(value));
    }
}
