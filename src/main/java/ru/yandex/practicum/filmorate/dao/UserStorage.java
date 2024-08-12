package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public User createUser(User user);

    public Optional<User> getUserById(long userId);

    public Collection<User> getUsers();

    void removeFriendship(long userId, long friendId);

    void createFriendship(long userId, long friendId);

}
