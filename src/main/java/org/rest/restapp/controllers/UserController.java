package org.rest.restapp.controllers;

import lombok.RequiredArgsConstructor;
import org.rest.restapp.models.UserDTO;
import org.rest.restapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addUser(@Validated @RequestBody UserDTO user) {
        this.userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDTO> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "7") Integer size) {
        UserDTO userFilter = UserDTO.builder()
                .name(name)
                .lastName(lastName)
                .email(email)
                .build();
        return this.userService.getUsers(userFilter, page, size);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        return this.userService.getUser(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUsers() {
        this.userService.removeUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer id) {
        this.userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
