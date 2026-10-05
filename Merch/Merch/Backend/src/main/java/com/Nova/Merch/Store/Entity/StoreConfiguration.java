package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
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
            foreignKey = @ForeignKey(name = "fk_store_configuration_store")
    )
  private Store store;

  protected StoreConfiguration () {}
  public StoreConfiguration (Store store) {
    this.store = store;
  }

 public Store getStore() {
   return store;
 } 
}
