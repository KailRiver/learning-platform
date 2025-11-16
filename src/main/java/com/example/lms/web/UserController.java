package com.example.lms.web;

import com.example.lms.dto.UserDTO;
import com.example.lms.entity.Role;
import com.example.lms.entity.User;
import com.example.lms.service.UserService;
import com.example.lms.service.DTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DTOMapper dtoMapper;

    public UserController(UserService userService, DTOMapper dtoMapper) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user.getName(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Role role) {
        List<UserDTO> users = userService.getUsersByRole(role).stream()
                .map(dtoMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(dtoMapper::toUserDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}