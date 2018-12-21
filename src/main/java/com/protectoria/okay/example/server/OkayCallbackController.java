package com.protectoria.okay.example.server;

import com.protectoria.gateway.tenant.model.callback.CallbackType;
import com.protectoria.gateway.tenant.model.callback.ServerAuthUserCallback;
import com.protectoria.gateway.tenant.model.callback.ServerCallback;
import com.protectoria.gateway.tenant.model.callback.ServerLinkUserCallback;
import com.protectoria.gateway.tenant.signature.HashingSignatureService;
import com.protectoria.gateway.tenant.signature.SignatureService;
import com.protectoria.gateway.tenant.signature.algorithm.Sha256HashingFunction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/callback")
@RestController
class OkayCallbackController {

    private final SignatureService signatureService = new HashingSignatureService(new Sha256HashingFunction());

    @RequestMapping
    public void handleServerCallback(@RequestBody ServerCallback callback) {
        final String token = "my-super-secret-token";

        if (callback.getType() == CallbackType.AUTH) {
            final ServerAuthUserCallback serverAuthUserCallback = (ServerAuthUserCallback) callback;
            final String expectedSignature = signatureService.generate(serverAuthUserCallback, token);
            if (!expectedSignature.equals(callback.getSignature())) {
                throw new IllegalStateException("Wrong signature");
            }
            System.out.println("Auth callback status: " + callback.getStatus().getMessage());
        } else if (callback.getType() == CallbackType.LINKING) {
            final ServerLinkUserCallback serverLinkUserCallback = (ServerLinkUserCallback) callback;
            final String expectedSignature = signatureService.generate(serverLinkUserCallback, token);
            if (!expectedSignature.equals(callback.getSignature())) {
                throw new IllegalStateException("Wrong signature");
            }
            System.out.println("Linking callback status: " + callback.getStatus().getMessage());
        }
    }

}
