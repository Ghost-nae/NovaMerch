package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "store_themes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_theme_store",
                        columnNames = "store_id"
                )
        }
)
public class StoreTheme extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_theme_store")
    )
  private Store store;

  protected StoreTheme() {}

  public StoreTheme(Store store) {
      this.store = store;
  }
  
  public Store getStore() {
    return store;
  }
}
