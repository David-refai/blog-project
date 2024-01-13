package com.example.backend.mapper;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Mapper class for mapping User to UserDto.
 * @see User
 * @see UserDto
 * @see Function
 * @see GrantedAuthority
 */
//@Service
//public class UserDTOMapper implements Function<User, UserDto> {
//    @Override
//    public UserDto apply(User user) {
//        return new UserDto(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                user.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList()),
//
//                user.getUsername()
//        );
//    }


//}
