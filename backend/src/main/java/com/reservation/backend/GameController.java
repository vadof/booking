package com.reservation.backend;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class GameController {
    private final UserRepository userRepository;

    public GameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/view/{id}")
    public Optional<TestUser> getUser(@PathVariable long id) {
        return userRepository.findById(id);
    }
}