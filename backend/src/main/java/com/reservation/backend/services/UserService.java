package com.reservation.backend.services;

import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.entities.User;
import com.reservation.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        // Convert DTO to Entity
        User user = new User(); // Assuming you have a User entity class
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());

        // Save Entity to database
        User savedUser = userRepository.save(user);

        // Convert Entity back to DTO
        UserDTO savedUserDTO = new UserDTO(savedUser.getId(), savedUser.getFirstname(),
                savedUser.getLastname(), savedUser.getEmail());
        return savedUserDTO;
    }
}