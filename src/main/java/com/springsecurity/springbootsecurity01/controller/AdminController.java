package com.springsecurity.springbootsecurity01.controller;

import com.springsecurity.springbootsecurity01.entity.Role;
import com.springsecurity.springbootsecurity01.entity.User;
import com.springsecurity.springbootsecurity01.repository.RoleRepository;
import com.springsecurity.springbootsecurity01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // GET ALL ROLES
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    // CREATE NEW ROLE
    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")  // Class-level security - all methods require ADMIN
    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest createRequest) {
        if (roleRepository.existsById(createRequest.roleName())) {
            return ResponseEntity.badRequest().build();
        }

        Role role = new Role();
        role.setRoleName(createRequest.roleName());
        role.setRoleDescription(createRequest.roleDescription());

        Role savedRole = roleRepository.save(role);
        return ResponseEntity.ok(savedRole);
    }

    // UPDATE USER ROLES
    @PutMapping("/users/{username}/roles")
    public ResponseEntity<User> updateUserRoles(
            @PathVariable String username,
            @RequestBody RoleUpdateRequest roleUpdateRequest) {

        Optional<User> existingUser = userRepository.findByUserName(username);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        Set<Role> newRoles = new HashSet<>();

        for (String roleName : roleUpdateRequest.roles()) {
            Optional<Role> role = roleRepository.findByRoleName(roleName);
            if (role.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            newRoles.add(role.get());
        }

        user.setRoles(newRoles);
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    // GET USERS BY ROLE
    @GetMapping("/roles/{roleName}/users")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String roleName) {
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        if (role.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role.get()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // SYSTEM STATISTICS - Only super admin can access
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('SUPER_ADMIN')")  // More restrictive than class-level
    public ResponseEntity<AdminStatistics> getSystemStatistics() {
        long totalUsers = userRepository.count();
        long enabledUsers = userRepository.findAll().stream()
                .filter(User::getEnabled)
                .count();
        List<Role> allRoles = roleRepository.findAll();

        AdminStatistics stats = new AdminStatistics(totalUsers, enabledUsers, allRoles);
        return ResponseEntity.ok(stats);
    }
}

// DTOs for Admin Controller
record RoleCreateRequest(String roleName, String roleDescription) {}
record RoleUpdateRequest(Set<String> roles) {}
record AdminStatistics(long totalUsers, long enabledUsers, List<Role> roles) {}