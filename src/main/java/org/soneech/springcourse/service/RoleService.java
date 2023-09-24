package org.soneech.springcourse.service;

import org.soneech.springcourse.model.Role;
import org.soneech.springcourse.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
