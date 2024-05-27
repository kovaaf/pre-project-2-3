package com.example.model.dto.mappers;

import com.example.exceptions.RoleNotFoundException;
import com.example.model.Role;
import com.example.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class RoleMapper {
    private final RoleRepository roleRepository;

    public Set<Role> convertToRoleSet(List<Long> roleIdList) {
        Set<Role> roleSet = new HashSet<>();
        if (roleIdList != null) {
            for (Long roleId : roleIdList) {
                roleSet.add(roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId.toString())));
            }
        }
        return roleSet;
    }
}
