package com.protectoria.okay.example.client;

import com.protectoria.gateway.tenant.model.AuthParamsDto;
import com.protectoria.gateway.tenant.model.ServerAuthCheckUserRequest;
import com.protectoria.gateway.tenant.model.ServerAuthCheckUserResponse;
import com.protectoria.gateway.tenant.model.ServerAuthUserRequest;
import com.protectoria.gateway.tenant.model.ServerAuthUserResponse;
import com.protectoria.gateway.tenant.model.ServerLinkUserRequest;
import com.protectoria.gateway.tenant.model.ServerLinkUserResponse;
import com.protectoria.gateway.tenant.model.status.TenantResponseStatusCode;
import com.protectoria.gateway.tenant.signature.HashingSignatureService;
import com.protectoria.gateway.tenant.signature.SignatureService;
import com.protectoria.gateway.tenant.signature.algorithm.Sha256HashingFunction;
import com.protectoria.pss.dto.session.SessionType;
import org.springframework.web.client.RestOperations;

public class OkayRestClient {

    private static final String OKAY_URI = "http://protdemo.demohoster.com/gateway";

    private RestOperations okayRestOperations;

    private Long tenantId;
    private String tenantSecretToken;

    private final SignatureService signatureService;

    public OkayRestClient() {
        this.signatureService = new HashingSignatureService(new Sha256HashingFunction());
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantSecretToken(String tenantSecretToken) {
        this.tenantSecretToken = tenantSecretToken;
    }

    public void setRestOperations(RestOperations okayRestOperations) {
        this.okayRestOperations = okayRestOperations;
    }

    public String linkUser(final String userExternalId) {
        final ServerLinkUserRequest request = new ServerLinkUserRequest();
        request.setUserExternalId(userExternalId);
        request.setTenantId(tenantId);
        request.setSignature(signatureService.generate(request, tenantSecretToken));

        final ServerLinkUserResponse linkUserResponse = okayRestOperations.postForObject(
                OKAY_URI + "/link", request, ServerLinkUserResponse.class);
        if (linkUserResponse.getStatus().getCode() != TenantResponseStatusCode.SUCCESS) {
            throw new IllegalStateException(linkUserResponse.getStatus().getMessage());
        }
        return linkUserResponse.getLinkingCode();
    }

    public String authUser(final String userExternalId, final SessionType type, final String header, final String text) {
        final ServerAuthUserRequest request = new ServerAuthUserRequest();
        request.setType(type);
        request.setUserExternalId(userExternalId);
        request.setTenantId(tenantId);

        final AuthParamsDto params = new AuthParamsDto();
        params.setGuiHeader(header);
        params.setGuiText(text);
        request.setAuthParams(params);

        request.setSignature(signatureService.generate(request, tenantSecretToken));

        final ServerAuthUserResponse response = okayRestOperations.postForObject(
                OKAY_URI + "/auth", request, ServerAuthUserResponse.class);
        if (response.getStatus().getCode() != TenantResponseStatusCode.SUCCESS) {
            throw new IllegalStateException(response.getStatus().getMessage());
        }
        return response.getSessionExternalId();
    }

    public String checkStatus(final String session) {
        final ServerAuthCheckUserRequest request = new ServerAuthCheckUserRequest();
        request.setSessionExternalId(session);
        request.setTenantId(tenantId);
        request.setSignature(signatureService.generate(request, tenantSecretToken));

        final ServerAuthCheckUserResponse response = okayRestOperations.postForObject(
                OKAY_URI + "/check", request, ServerAuthCheckUserResponse.class);
        if (response.getStatus().getCode() != TenantResponseStatusCode.SUCCESS
                && response.getStatus().getCode() != TenantResponseStatusCode.INCOMPLETE) {
            throw new IllegalStateException(response.getStatus().getMessage());
        }
        return response.getAuthResult().getData();
    }

}
