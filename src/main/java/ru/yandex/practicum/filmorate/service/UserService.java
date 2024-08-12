package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        try {
            UserValidator.isValid(user);
            log.info("Создание пользователя с ID {}", user.getId());
            return userStorage.createUser(user);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public User updateUser(User user) {
        try {
            UserValidator.isValid(user);
            if (Objects.isNull(user.getId())) {
                throw new ValidationException("Id должен быть указан");
            }
            Long id = user.getId();
            User oldUser = userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());
            log.info("Обновление пользователя с ID {}", id);
            return userStorage.createUser(oldUser);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public void addFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        if (userStorage.getUserById(friendId).isEmpty()) {
            throw new UserNotFoundException(friendId);
        }
        log.info("Пользователь {} отправил запрос в друзья пользователю{}", id, friendId);
        userStorage.createFriendship(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        if (userStorage.getUserById(friendId).isEmpty()) {
            throw new NotFoundException(friendId);
        }
        log.info("Пользователь {} удалил пользователя {} из друзей", id, friendId);
        userStorage.removeFriendship(id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Получение списка друзей пользователя {} ", id);
        User user = userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long id, Long secondId) {
        log.info("Получение общих друзей у пользователя {} и пользователя {}", id, secondId);
        Set<Long> firstUserFriends =
                userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException(id)).getFriends();
        Set<Long> secondUserFriends =
                userStorage.getUserById(secondId).orElseThrow(() -> new UserNotFoundException(secondId))
                        .getFriends();
        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::getUserById)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
    }


}
