package com.protectoria.okay.example.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.protectoria.gateway.tenant.model.callback.CallbackType;

public class CallbackTypeJsonDeserializer extends JsonDeserializer<CallbackType> {

    @Override
    public Class<CallbackType> handledType() {
        return CallbackType.class;
    }

    @Override
    public CallbackType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final int value = p.getIntValue();
        for (CallbackType code : CallbackType.values()) {
            if (code.getCode() == value) return code;
        }
        return null;
    }

}
