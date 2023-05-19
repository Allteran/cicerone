package io.allteran.cicerone.backbone.util;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.dto.RoleDTO;
import org.springframework.beans.BeanUtils;

public class EntityConverter {
    public static RoleDTO convertToDTO(Role entity) {
        RoleDTO dto = new RoleDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Role convertToEntity(RoleDTO dto) {
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        return role;
    }
}
