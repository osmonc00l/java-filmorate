package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    private final long id;
    String message;

    public NotFoundException(long id) {
        this.id = id;
    }
}
