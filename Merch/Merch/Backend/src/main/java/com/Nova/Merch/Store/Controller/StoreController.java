package com.Nova.Merch.Store.Controller;

import com.Nova.Merch.Store.DTO.CreateStoreRequest;
import com.Nova.Merch.Store.DTO.StoreResponse;
import com.Nova.Merch.Store.Entity.Store;
import com.Nova.Merch.Store.Service.StoreProvisioningService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platform/tenants/{tenantId}/stores")
public class StoreController {

    private final StoreProvisioningService storeProvisioningService;

    public StoreController(
            StoreProvisioningService storeProvisioningService
    ) {
        this.storeProvisioningService = storeProvisioningService;
    }

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateStoreRequest request
    ) {

        Store store = storeProvisioningService.provisionStore(
                tenantId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StoreResponse.from(store));
    }
}
