package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        String isValid = checkIfValid(user);

        if (!isValid.isEmpty()) {
            throw new ValidationException(isValid);
        }

        user.setId(getNextId());
        log.debug("Новому пользователю присвоен id {}", user.getId());
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.debug("Пользователь с id {} был добавлен", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        String isValid = checkIfValid(newUser);
        if (!isValid.isEmpty()) {
            throw new ValidationException(isValid);
        }
        log.info("Валидация прошла успешно");
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            log.debug("Почтовый ящик пользователя {} изменен на {}", oldUser.getId(), oldUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            log.debug("Логин пользователя {} был изменен на {}", oldUser.getId(), oldUser.getLogin());
            oldUser.setName(newUser.getName());
            log.debug("Имя пользователя {} был изменен на {}", oldUser.getId(), oldUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.debug("Дата рождения пользователя {} был изменен на {}", oldUser.getId(), oldUser.getBirthday());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private String checkIfValid(User user) {
        if (Objects.isNull(user.getEmail()) || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            return "Электронная почта не может быть пустой и должна содержать символ @";
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return "Логин не может быть пустым и содержать пробелы";
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            return "Дата рождения не может быть в будущем.";
        } else {
            return "";
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
