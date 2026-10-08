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
        name = "store_settings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_settings_store",
                        columnNames = "store_id"
                )
        }
)
public class StoreSettings extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_settings_store")
    )
    @Setter(AccessLevel.NONE)
    private Store store;

    @Column(name = "default_currency", nullable = false, length = 3)
    private String defaultCurrency = "ZAR";

    @Column(name = "default_language", nullable = false, length = 10)
    private String defaultLanguage = "en";

    @Column(name = "timezone", nullable = false, length = 100)
    private String timezone = "Africa/Johannesburg";

    @Column(name = "order_prefix", nullable = false, length = 20)
    private String orderPrefix = "ORD";

    @Column(name = "customer_registration_enabled", nullable = false)
    private boolean customerRegistrationEnabled = true;

    @Column(name = "guest_checkout_enabled", nullable = false)
    private boolean guestCheckoutEnabled = false;

    public StoreSettings(Store store) {
        this.store = store;
    }
}
