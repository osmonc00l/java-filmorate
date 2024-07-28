package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        try {
            UserValidator.isValid(user);
            return userStorage.createUser(user);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public User updateUser(User newUser) {
        try {
            UserValidator.isValid(newUser);
            return userStorage.updateUser(newUser);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public User addFriend(Long id, Long friendId) {
        doesUserExist(id, friendId);
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        return friend;
    }

    public User deleteFriend(Long id, Long friendId) {
        doesUserExist(id, friendId);
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        return friend;
    }

    public Collection<User> getFriends(Long id) {
        doesUserExist(id);
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long id, Long secondId) {
        doesUserExist(id, secondId);
        Set<Long> firstUserFriends = userStorage.getUserById(id).getFriends();
        Set<Long> secondUserFriends = userStorage.getUserById(secondId).getFriends();
        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void doesUserExist(long... listId) {
        for (long id : listId) {
            if (!userStorage.doesUserExist(id)) {
                throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
            }
        }
    }

}
