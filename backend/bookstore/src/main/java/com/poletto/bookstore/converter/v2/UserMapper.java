package com.poletto.bookstore.converter.v2;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
	
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "id", source = "id")
	UserDTOv2 userToUserDto(User user);
	
	@Mapping(target = "id", source = "id")
	User userDtoToUser(UserDTOv2 userDto);
	
	@Mapping(target = "id", source = "id")
	UserAuthDTOv2 userToUserAuthDto(User user);

}