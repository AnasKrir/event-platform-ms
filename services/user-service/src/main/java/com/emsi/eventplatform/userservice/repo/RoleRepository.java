package com.emsi.eventplatform.userservice.repo;

import com.emsi.eventplatform.userservice.models.Role;
import com.emsi.eventplatform.userservice.models.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
