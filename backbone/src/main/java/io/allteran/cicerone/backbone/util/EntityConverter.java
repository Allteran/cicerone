package io.allteran.cicerone.backbone.util;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.domain.User;
import io.allteran.cicerone.backbone.dto.RoleDTO;
import io.allteran.cicerone.backbone.dto.UserDTO;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

public class EntityConverter {
    public static RoleDTO convertToDTO(Role entity) {
        RoleDTO dto = new RoleDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Role convertToEntity(RoleDTO dto) {
        Role entity = new Role();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    //WARNING: using raw this method can call NPE, because we don't set roles as well from Set of Role.id
    public static User convertToEntity (UserDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static UserDTO convertToDTO(User entity) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
