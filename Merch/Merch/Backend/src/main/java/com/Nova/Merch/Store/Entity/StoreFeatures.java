package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
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
            foreignKey = @ForeignKey(name = "fk_store_features_store")
    )
  private Store store;

  protected StoreFeatures() {}

  public StoreFeatures(Store store) {
    this.store = store;
  }
  public Store getStore() {
    return store;
  }
}
