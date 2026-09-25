package com.Nova.Merch.Identity.Repository;

import com.Nova.Merch.Identity.Entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface IdentityRepository extends JpaRepository<Identity, UUID> {
  Optional<Identity> findByEmail(String email);
  boolean existsByEmail(String email);
}
