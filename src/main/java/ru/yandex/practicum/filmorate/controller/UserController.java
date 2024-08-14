package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity
                .status(200)
                .body(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        log.info("Получение пользователя с id: {}", id);
        return ResponseEntity
                .status(200)
                .body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(200)
                .body(userService.createUser(userDto));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid@RequestBody UserDto user) {
        return ResponseEntity
                .status(200)
                .body(userService.updateUser(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return ResponseEntity
                .status(200)
                .build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        return ResponseEntity
                .status(200)
                .build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<UserDto>> getFriends(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity
                .status(200)
                .body(userService.getCommonFriends(id, otherId));
    }
}
