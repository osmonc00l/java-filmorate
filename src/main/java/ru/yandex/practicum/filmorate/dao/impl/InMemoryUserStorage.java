package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        log.info("Новому пользователю присвоен id {}", user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с id {} был добавлен", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User removeUser(User user) {
        log.info("Удаление фильма {}", user);
        return users.remove(user.getId());
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        log.info("Поиск пользователя по ID {}", userId);
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void removeFriendship(long userId, long friendId) {
        log.info("Удаление пользователя по ID {}", userId);
        users.get(userId).getFriends().remove(friendId);
    }

    @Override
    public void createFriendship(long userId, long friendId) {
        log.info("Создание дружбы между пользователями: {} и {}", userId, friendId);
        users.get(userId).getFriends().add(friendId);
    }

    @Override
    public List<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(users.values());
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
