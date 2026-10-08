package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "store_configurations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_configuration_store",
                        columnNames = "store_id"
                )
        }
)
public class StoreConfiguration extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_store_configuration_store"
            )
    )
    private Store store;

    @Column(
            name = "configuration_version",
            nullable = false
    )
    private Integer configurationVersion = 1;

    @Column(
            name = "configuration_completed",
            nullable = false
    )
    private boolean configurationCompleted = false;

    @Column(name = "last_published_at")
    private Instant lastPublishedAt;

    public StoreConfiguration(Store store) {
        this.store = store;
    }

    public void markConfigurationCompleted() {
        this.configurationCompleted = true;
        incrementVersion();
    }

    public void markConfigurationIncomplete() {
        this.configurationCompleted = false;
        incrementVersion();
    }

    public void markAsPublished() {
        if (!configurationCompleted) {
            throw new IllegalStateException(
                    "Store configuration must be completed before publishing."
            );
        }

        this.lastPublishedAt = Instant.now();
        incrementVersion();
    }

    private void incrementVersion() {
        this.configurationVersion++;
    }
}
