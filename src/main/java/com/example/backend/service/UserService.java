package com.example.backend.service;


import com.example.individuelluppgiftspringboot.dao.RoleRepository;
import com.example.individuelluppgiftspringboot.dao.UserRepository;
import com.example.individuelluppgiftspringboot.dto.userdto.UserDto;
import com.example.individuelluppgiftspringboot.dto.userdto.UserRegistrationDTO;
import com.example.individuelluppgiftspringboot.entities.Role;
import com.example.individuelluppgiftspringboot.entities.User;
import com.example.individuelluppgiftspringboot.exception.ExistsEmailException;
import com.example.individuelluppgiftspringboot.exception.HandleMethodArgumentNotValid;
import com.example.individuelluppgiftspringboot.exception.ResourceNotFoundException;
import com.example.individuelluppgiftspringboot.mapper.UserDTOMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service class for managing users.
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final UserDTOMapper userDTOMapper;
    private Long id;
    private UserRegistrationDTO userRegistrationRequest;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDTOMapper = userDTOMapper;
    }




    /**
     * Save a new user.
     * @param userRegistrationDTO The user to save.
     * @return The saved user.
     */
    public User saveUserWithRoles(UserRegistrationDTO userRegistrationDTO) {
// Validate the UserDto (e.g., check for required fields)
        validateUserDto(userRegistrationDTO);

//        check if email already exists
        existsByEmail(userRegistrationDTO.getEmail());


        User user = new User();
        user.setName(userRegistrationDTO.getName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        if (userRegistrationDTO.getRoles() == null) {
            userRegistrationDTO.setRoles(List.of("USER"));
        }

        var roles = userRegistrationDTO.getRoles();
        roles.forEach(roleName -> {
            Role role = new Role();
            role.setName(roleName);
            role.setUser(user);
            user.addRole(role);
        });

        return userRepository.save(user);

    }


    /**
     * Get all users.
     * @return A list of all users.
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());

    }

    /**
     * Get the user with the specified ID.
     * @param id The ID of the user to get.
     * @return The user with the specified ID.
     * @throws ResourceNotFoundException if the user is not found.
     */

    public UserDto getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userDTOMapper.apply(user.get());
        } else {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
    }


    /**
     * Check if the specified email address already exists.
     * @param email The email address to check.
     * @return true if the email address already exists, otherwise false.
     */
    public boolean existsByEmail(String email) {
    userRepository.getByEmail(email)
                .ifPresent(user -> {
                    throw new ExistsEmailException("Email already exists");
                });
        return false;
    }

    /**
     * Validate the UserDto (e.g., check for required fields)
     * @param user The UserDto to validate.
     * @throws HandleMethodArgumentNotValid if the UserDto is invalid.
      */
    private void validateUserDto(@Valid UserRegistrationDTO user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new HandleMethodArgumentNotValid("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new HandleMethodArgumentNotValid("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new HandleMethodArgumentNotValid("Password is required");
        }

    }



    /**
     * Update the user with the specified ID.
     * @param id The ID of the user to update.
     * @param userRegistrationRequest The updated user information.
     * @return The updated user.
     * @throws ResourceNotFoundException if the user is not found.
     * @throws ExistsEmailException if the email already exists.
     *
     */
    public UserDto updateUser (Long id, UserRegistrationDTO userRegistrationRequest) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
            boolean updated = false;

            if (userRegistrationRequest.getName() != null && !userRegistrationRequest.getName().equals(user.getName())) {
                user.setName(userRegistrationRequest.getName());
                updated = true;
            }
            // Check if a new password is provided
            if (userRegistrationRequest.getPassword() != null && !userRegistrationRequest.getPassword().isEmpty()) {
//                updatePassword(user.getEmail(), userRegistrationRequest.getPassword());
                user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
                updated = true;
            }

            // Check if a new role is provided
            if (userRegistrationRequest.getRoles() != null && !userRegistrationRequest.getRoles().isEmpty()) {
                user.setRoles(userRegistrationRequest.getRoles().stream().map(Role::new).collect(Collectors.toList()));
                updated = true;
            }

            // Check if a new email is provided
            if (userRegistrationRequest.getEmail() != null && !userRegistrationRequest.getEmail().equals(user.getEmail())) {
                existsByEmail(userRegistrationRequest.getEmail());
                user.setEmail(userRegistrationRequest.getEmail());
                updated = true;
            }else if(userRegistrationRequest.getEmail().equals(user.getEmail())){
                updated = true;
            }
            if (!updated) {
                throw new ExistsEmailException("No changes were made");
            }


          User userUpdate =  userRepository.save(user);
        System.out.println(userUpdate);
          return userDTOMapper.apply(userUpdate);

    }

    /**
     * Delete the user with the specified ID.
     * @param id The ID of the user to delete.
     * @return The deleted user.
     * @throws ResourceNotFoundException if the user is not found.
     */
    public Optional<User> deleteUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return optionalUser;
        } else {
            // Handle the case where the user with the specified ID is not found
            throw new HandleMethodArgumentNotValid("User not found with ID: " + id);
        }
    }


}
