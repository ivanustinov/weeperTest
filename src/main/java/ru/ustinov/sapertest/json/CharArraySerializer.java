package ru.ustinov.sapertest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 10.02.2024
 */
public class CharArraySerializer extends JsonSerializer<char[]> {

    @Override
    public void serialize(char[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (char c : value) {
            gen.writeString(String.valueOf(c));
        }
        gen.writeEndArray();
    }
}
