package com.Nova.Merch.Tenant.Controller;

import com.Nova.Merch.Tenant.DTO.CreateTenantRequest;
import com.Nova.Merch.Tenant.DTO.TenantResponse;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Service.TenantProvisioningService;
import com.Nova.Merch.Tenant.Service.TenantActivationService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform/tenants")
public class TenantController {

    private final TenantProvisioningService tenantProvisioningService;
    private final TenantActivationService tenantActivationService;

    public TenantController(
            TenantProvisioningService tenantProvisioningService, TenantActivationService tenantActivationService
    ) {
        this.tenantProvisioningService = tenantProvisioningService;
        this.tenantActivationService = tenantActivationService;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {

        Tenant tenant = tenantProvisioningService
                .provisionTenant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TenantResponse.from(tenant));
    }

    @PatchMapping("/{tenantId}/activate")
    public ResponseEntity<TenantResponse> activateTenant(
            @PathVariable UUID tenantId
    ) {
        Tenant tenant = tenantActivationService
                .activateTenant(tenantId);

        return ResponseEntity.ok(
                TenantResponse.from(tenant)
        );
    }
}
