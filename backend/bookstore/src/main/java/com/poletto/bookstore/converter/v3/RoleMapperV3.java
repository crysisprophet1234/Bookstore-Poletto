package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.entities.Role;

@Component
@Mapper(componentModel = "spring")
public interface RoleMapperV3 {
	
	RoleMapperV3 INSTANCE = Mappers.getMapper(RoleMapperV3.class);

	@Mapping(target = "id", source = "id")
	RoleDto roleToRoleDto(Role role);
	
	@Mapping(target = "id", source = "id")
	Role roleDtoToRole(RoleDto roleDto);
	
}
