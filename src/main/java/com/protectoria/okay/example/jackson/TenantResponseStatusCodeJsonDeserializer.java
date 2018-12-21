package com.protectoria.okay.example.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.protectoria.gateway.tenant.model.status.TenantResponseStatusCode;

public class TenantResponseStatusCodeJsonDeserializer extends JsonDeserializer<TenantResponseStatusCode> {

    @Override
    public Class<TenantResponseStatusCode> handledType() {
        return TenantResponseStatusCode.class;
    }

    @Override
    public TenantResponseStatusCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final int value = p.getIntValue();
        for (TenantResponseStatusCode code : TenantResponseStatusCode.values()) {
            if (code.getCode() == value) return code;
        }
        return null;
    }

}
