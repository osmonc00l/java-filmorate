package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long userId);

    List<User> getUsers();

    Long findMaxId();

    void removeFriendship(long userId, long friendId);

    void createFriendship(long userId, long friendId);

}
