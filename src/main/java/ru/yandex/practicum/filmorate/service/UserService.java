package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
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

    public List<UserDto> getUsers() {
        return UserMapper.INSTANCE.toDto(userStorage.getUsers());
    }

    public UserDto getUserById(long id) {
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            log.error("Пользователь с id {} не найден", id);
            throw new NotFoundException(id);
        }
        return UserMapper.INSTANCE.toDto(user.get());
    }

    public UserDto createUser(UserDto userDto) {
        UserValidator.isValid(userDto);
        log.info("Создание пользователя с ID {}", userDto.getId());
        System.out.println(userDto.getFriends());
        User user = UserMapper.INSTANCE.toEntity(userDto);
        return UserMapper.INSTANCE.toDto(userStorage.createUser(user));
    }

    public UserDto updateUser(UserDto userDto) {
        UserValidator.isValid(userDto);
        Long id = userDto.getId();
        System.out.println(id);
        User foundUser = userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        foundUser.setEmail(userDto.getEmail());
        foundUser.setLogin(userDto.getLogin());
        foundUser.setName(userDto.getName());
        foundUser.setBirthday(userDto.getBirthday());
        log.info("Обновление пользователя с ID {}", id);
        return UserMapper.INSTANCE.toDto(userStorage.updateUser(foundUser));
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

    public Collection<UserDto> getFriends(Long id) {
        log.info("Получение списка друзей пользователя {} ", id);
        User user = userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .map(Optional::orElseThrow)
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public Collection<UserDto> getCommonFriends(Long id, Long secondId) {
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
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }


}
