package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        log.debug("Новому пользователю присвоен id {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь с id {} был добавлен", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }

    @Override
    public User removeUser(User user) {
        return users.remove(user.getId());
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public boolean doesUserExist(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
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
