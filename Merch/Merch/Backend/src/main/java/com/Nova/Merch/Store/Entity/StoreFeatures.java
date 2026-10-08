package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "store_features",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_features_store",
                        columnNames = "store_id"
                )
        }
)
public class StoreFeatures extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_store_features_store"
            )
    )
    @Setter(AccessLevel.NONE)
    private Store store;

    @Column(name = "ecommerce_enabled", nullable = false)
    private boolean ecommerceEnabled = true;

    @Column(name = "online_payments_enabled", nullable = false)
    private boolean onlinePaymentsEnabled = false;

    @Column(name = "delivery_enabled", nullable = false)
    private boolean deliveryEnabled = false;

    @Column(name = "pickup_enabled", nullable = false)
    private boolean pickupEnabled = false;

    @Column(name = "reviews_enabled", nullable = false)
    private boolean reviewsEnabled = false;

    @Column(name = "discounts_enabled", nullable = false)
    private boolean discountsEnabled = false;

    @Column(name = "inventory_enabled", nullable = false)
    private boolean inventoryEnabled = true;

    @Column(name = "subscriptions_enabled", nullable = false)
    private boolean subscriptionsEnabled = false;

    public StoreFeatures(Store store) {
        this.store = store;
    }
}
