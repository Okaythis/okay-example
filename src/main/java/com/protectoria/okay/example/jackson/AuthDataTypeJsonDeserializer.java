package com.protectoria.okay.example.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.protectoria.gateway.tenant.model.callback.AuthDataType;

public class AuthDataTypeJsonDeserializer extends JsonDeserializer<AuthDataType> {

    @Override
    public Class<AuthDataType> handledType() {
        return AuthDataType.class;
    }

    @Override
    public AuthDataType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final int value = p.getIntValue();
        for (AuthDataType code : AuthDataType.values()) {
            if (code.getCode() == value) return code;
        }
        return null;
    }

}
