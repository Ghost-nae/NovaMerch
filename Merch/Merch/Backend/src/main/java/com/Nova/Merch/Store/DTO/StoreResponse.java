package com.Nova.Merch.Store.DTO;

import com.Nova.Merch.Store.Entity.Store;
import com.Nova.Merch.Store.Model.StoreStatus;

import java.time.Instant;
import java.util.UUID;

public record StoreResponse(
        UUID id,
        UUID tenantId,
        String name,
        String code,
        StoreStatus status,
        Instant createdAt
) {

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getTenant().getId(),
                store.getName(),
                store.getCode(),
                store.getStatus(),
                store.getCreatedAt()
        );
    }
}
