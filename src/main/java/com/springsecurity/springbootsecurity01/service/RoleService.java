package com.springsecurity.springbootsecurity01.service;



import com.springsecurity.springbootsecurity01.dtos.RoleDTO;
import com.springsecurity.springbootsecurity01.entity.Role;
import com.springsecurity.springbootsecurity01.repository.RoleRepository;
import com.springsecurity.springbootsecurity01.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> findAll() {
        final List<Role> roles = roleRepository.findAll(Sort.by("roleName"));
        return roles.stream()
                .map((role01) -> mapToDTO(role01, new RoleDTO()))
                .toList();
    }

    public RoleDTO get(final String roleName) {
        return roleRepository.findById(roleName)
                .map(role01 -> mapToDTO(role01, new RoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final RoleDTO roleDTO) {
        final Role role = new Role();
        mapToEntity(roleDTO, role);
        role.setRoleName(roleDTO.getRoleName());
        return roleRepository.save(role).getRoleName();
    }

    public void update(final String roleName, final RoleDTO roleDTO) {
        final Role role = roleRepository.findById(roleName)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roleDTO, role);
        roleRepository.save(role);
    }

    public void delete(final String roleName) {
        roleRepository.deleteById(roleName);
    }

    private RoleDTO mapToDTO(final Role role, final RoleDTO roleDTO) {
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setRoleDescription(role.getRoleDescription());
        return roleDTO;
    }

    private Role mapToEntity(final RoleDTO roleDTO, final Role role) {
        role.setRoleDescription(roleDTO.getRoleDescription());
        return role;
    }

    public boolean roleNameExists(final String roleName) {
        return roleRepository.existsByRoleNameIgnoreCase(roleName);
    }

}
