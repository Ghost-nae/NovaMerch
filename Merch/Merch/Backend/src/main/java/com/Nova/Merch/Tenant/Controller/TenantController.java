package com.Nova.Merch.Tenant.Controller;

import com.Nova.Merch.Tenant.DTO.CreateTenantRequest;
import com.Nova.Merch.Tenant.DTO.TenantResponse;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Service.TenantProvisioningService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform/tenants")
public class TenantController {

    private final TenantProvisioningService tenantProvisioningService;

    public TenantController(
            TenantProvisioningService tenantProvisioningService
    ) {
        this.tenantProvisioningService = tenantProvisioningService;
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
}
