package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
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
    private Store store;

    protected StoreSettings() {
    }

    public StoreSettings(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }
}
