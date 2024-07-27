package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void shouldNotPassValidationByEmail() {
        User someUser = new User();
        someUser.setEmail("grewteryahoo.com");
        someUser.setLogin("werwe");
        someUser.setName("fasdfas");
        someUser.setBirthday(LocalDate.of(2003, 12, 6));

        assertThrows(ValidationException.class, () -> userController.createUser(someUser));

        assertTrue(userController.findAllUsers().isEmpty());
    }

    @Test
    void shouldNotPassValidationByLogin() {
        User someUser = new User();
        someUser.setEmail("sdagasr@gmail.ru");
        someUser.setLogin("Kott Pushok");
        someUser.setName("dfsghdf");
        someUser.setBirthday(LocalDate.of(2001, 6, 12));

        assertThrows(ValidationException.class, () -> userController.createUser(someUser));

        assertTrue(userController.findAllUsers().isEmpty());
    }

    @Test
    void shouldNotPassValidationByBirthday() {
        User someUser = new User();
        someUser.setEmail("sdgfhjsdhf@yandex.ru");
        someUser.setLogin("htreyr");
        someUser.setName("trsrtghnrt");
        someUser.setBirthday(LocalDate.now().plusDays(15));

        assertThrows(ValidationException.class, () -> userController.createUser(someUser));

        assertTrue(userController.findAllUsers().isEmpty());
    }
}
