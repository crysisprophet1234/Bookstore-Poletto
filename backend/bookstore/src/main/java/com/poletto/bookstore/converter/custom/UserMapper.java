package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.RoleDTO;
import com.poletto.bookstore.dto.v1.UserAuthDTO;
import com.poletto.bookstore.dto.v1.UserDTO;
import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;

@Service
public class UserMapper {

	public static UserAuthDTO convertEntityToAuthDto(User user) {
		
		UserAuthDTO dto = new UserAuthDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		dto.setPassword(user.getPassword());
		
		for (Role role : user.getRoles()) {
			dto.getRoles().add(DozerMapperConverter.parseObject(role, RoleDTO.class));
		}
		
		return dto;
		
	}
	
	public static UserDTO convertEntityToDto(User user) {
		
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstname(user.getFirstname());
		dto.setLastname(user.getLastname());
		
		for (Role role : user.getRoles()) {
			dto.getRoles().add(DozerMapperConverter.parseObject(role, RoleDTO.class));
		}
		
		return dto;
		
	}
	
	public static User convertDtoToEntity (UserDTO dto) {
		
		User user = new User();
		user.setId(dto.getId());
		user.setEmail(dto.getEmail());
		user.setFirstname(dto.getFirstname());
		user.setLastname(dto.getLastname());
		
		for (RoleDTO roleDTO : dto.getRoles()) {
			user.getRoles().add(DozerMapperConverter.parseObject(roleDTO, Role.class));
		}
		
		return user;
		
	}

}
