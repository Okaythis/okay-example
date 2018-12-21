package com.protectoria.okay.example.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.protectoria.gateway.tenant.model.callback.CallbackType;
import com.protectoria.gateway.tenant.model.callback.ServerAuthUserCallback;
import com.protectoria.gateway.tenant.model.callback.ServerCallback;
import com.protectoria.gateway.tenant.model.callback.ServerLinkUserCallback;

public class ServerCallbackJsonDeserializer extends JsonDeserializer<ServerCallback> {

    @Override
    public Class<ServerCallback> handledType() {
        return ServerCallback.class;
    }

    @Override
    public ServerCallback deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final TreeNode treeNode = p.readValueAsTree();
        final int type = ((IntNode) treeNode.get("type")).asInt();

        if (type == CallbackType.AUTH.getCode()) {
            return p.getCodec().treeToValue(treeNode, ServerAuthUserCallback.class);
        }

        if (type == CallbackType.LINKING.getCode()) {
            return p.getCodec().treeToValue(treeNode, ServerLinkUserCallback.class);
        }

        return null;
    }

}
