package com.Nova.Merch.Identity.Repository;

import com.Nova.Merch.Identity.Entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdentityRepository extends JpaRepository<Identity, UUID> {
  Optional<Identity> findByEmail(String email);
  boolean existsByEmail(String email);
  
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      SELECT identity
      FROM Identity identity
      WHERE identity.id = :identityId
  """)
  Optional<Identity> findByIdForUpdate(
          @Param("identityId") UUID identityId
  );
}
