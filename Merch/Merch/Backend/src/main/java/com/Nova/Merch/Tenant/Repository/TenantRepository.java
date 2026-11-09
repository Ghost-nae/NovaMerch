package com.Nova.Merch.Tenant.Repository;

import com.Nova.Merch.Tenant.Entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
  Optional<Tenant> findByCode(String code);
  boolean existsByCode(String code);

  
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      SELECT tenant
      FROM Tenant tenant
      WHERE tenant.id = :tenantId
  """)
  Optional<Tenant> findByIdForUpdate(
          @Param("tenantId") UUID tenantId
  );

}
