package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.RoleDTOv1;
import com.poletto.bookstore.dto.v1.UserAuthDTOv1;
import com.poletto.bookstore.dto.v1.UserDTOv1;
import com.poletto.bookstore.dto.v2.RoleDTOv2;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;

@Service
public class UserMapper {

	public static UserAuthDTOv1 convertEntityToAuthDto(User user) {
		
		UserAuthDTOv1 dto = new UserAuthDTOv1();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		dto.setPassword(user.getPassword());
		
		for (Role role : user.getRoles()) {
			dto.getRoles().add(DozerMapperConverter.parseObject(role, RoleDTOv1.class));
		}
		
		return dto;
		
	}
	
	public static UserAuthDTOv2 convertEntityToAuthDtoV2(User user) {
		
		UserAuthDTOv2 dto = new UserAuthDTOv2();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		dto.setPassword(user.getPassword());
		
		for (Role role : user.getRoles()) {
			dto.getRoles().add(DozerMapperConverter.parseObject(role, RoleDTOv2.class));
		}
		
		return dto;
		
	}
	
	public static UserDTOv1 convertEntityToDto(User user) {
		
		UserDTOv1 dto = new UserDTOv1();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		
		for (Role role : user.getRoles()) {
			dto.getRoles().add(DozerMapperConverter.parseObject(role, RoleDTOv1.class));
		}
		
		return dto;
		
	}
	
	public static UserDTOv2 convertEntityToDtoV2(User user) {
		
		UserDTOv2 dto = new UserDTOv2();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		
		for (Role role : user.getRoles()) {
			RoleDTOv2 roleDTO = DozerMapperConverter.parseObject(role, RoleDTOv2.class);
			roleDTO.setId(role.getId());
			dto.getRoles().add(roleDTO);
		}
		
		return dto;
		
	}
	
	public static User convertDtoToEntity (UserDTOv1 dto) {
		
		User user = new User();
		user.setId(dto.getId());
		user.setEmail(dto.getEmail());
		user.setFirstname(dto.getFirstname());
		user.setLastname(dto.getLastname());
		
		for (RoleDTOv1 roleDTO : dto.getRoles()) {
			user.getRoles().add(DozerMapperConverter.parseObject(roleDTO, Role.class));
		}
		
		return user;
		
	}
	
	public static User convertDtoToEntityV2 (UserDTOv2 dto) {
		
		User user = new User();
		user.setId(dto.getId());
		user.setEmail(dto.getEmail());
		user.setFirstname(dto.getFirstname());
		user.setLastname(dto.getLastname());
		
		for (RoleDTOv2 roleDTO : dto.getRoles()) {
			user.getRoles().add(DozerMapperConverter.parseObject(roleDTO, Role.class));
		}
		
		return user;
		
	}

}
