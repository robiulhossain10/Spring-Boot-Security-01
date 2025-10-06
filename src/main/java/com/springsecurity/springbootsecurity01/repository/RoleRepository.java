package com.springsecurity.springbootsecurity01.repository;



import com.springsecurity.springbootsecurity01.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);
}