package ru.yandex.practicum.filmorate.exception;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}
