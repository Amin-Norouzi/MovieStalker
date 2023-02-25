package com.aminnorouzi.ms.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class ImdbUrlConverter extends JsonDeserializer<String> {

    @Value("${movie.client.api.imdb.base-url}")
    private String base;

    @Value("${movie.client.api.external-ids-field.imdbId}")
    private String field;

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);
        final String value = node.findValue(field).asText();

        return base.concat(value);
    }
}
