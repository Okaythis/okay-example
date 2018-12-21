package com.protectoria.okay.example.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.protectoria.pss.dto.converter.EnumWithCode;

public class EnumJsonSerializer extends JsonSerializer<EnumWithCode> {

    @Override
    public Class<EnumWithCode> handledType() {
        return EnumWithCode.class;
    }

    @Override
    public void serialize(
            EnumWithCode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(value.getCode());
        }
    }

}
