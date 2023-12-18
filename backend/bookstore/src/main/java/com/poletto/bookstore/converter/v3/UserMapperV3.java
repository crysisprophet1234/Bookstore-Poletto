package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapperV3 {
	
	UserMapperV3 INSTANCE = Mappers.getMapper(UserMapperV3.class);

	@Mapping(target = "key", source = "id")
	@Mapping(target = "token", ignore = true)
	UserDto userToUserDto(User user);
	
	@Mapping(target = "id", source = "key")
	User userDtoToUser(UserDto userDto);
	
}