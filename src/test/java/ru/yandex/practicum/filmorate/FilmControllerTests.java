package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
class FilmControllerTests {

	private static FilmService filmService;

	@BeforeEach
	void beforeEach() {
		filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
	}

	@Test
	void shouldNotPassValidationByName() {
		Film someFilm = new Film();
		someFilm.setName("");
		someFilm.setDescription("Молчаливый водитель спасает девушку от гангстеров. Неонуар с Райаном Гослингом " +
						"и пульсирующим саундтреком");
		someFilm.setReleaseDate(LocalDate.of(2011, 5, 20));
		someFilm.setDuration(100);

		assertThrows(ValidationException.class, () -> filmService.createFilm(someFilm));

		assertFalse(filmService.getFilms().contains(someFilm));
	}

	@Test
	void shouldNotPassValidationByDescription() {
		Film someFilm = new Film();
		someFilm.setName("Отступники");
		someFilm.setDescription("Два лучших выпускника полицейской академии оказались по разные стороны баррикады: " +
						"один из них — агент мафии в рядах правоохранительных органов, другой — «крот», внедрённый " +
						"в мафию. Каждый считает своим долгом обнаружить и уничтожить противника.");
		someFilm.setReleaseDate(LocalDate.of(2006, 9, 26));
		someFilm.setDuration(151);

		assertThrows(ValidationException.class, () -> filmService.createFilm(someFilm));

		assertFalse(filmService.getFilms().contains(someFilm));
	}

	@Test
	void shouldNotPassValidationByDateTime() {
		Film someFilm = new Film();
		someFilm.setName("Фильм");
		someFilm.setDescription("123");
		someFilm.setReleaseDate(LocalDate.of(1861, 12, 4));
		someFilm.setDuration(104);

		assertThrows(ValidationException.class, () -> filmService.createFilm(someFilm));

		assertFalse(filmService.getFilms().contains(someFilm));
	}

	@Test
	void shouldNotPassValidationByDuration() {
		Film someFilm = new Film();
		someFilm.setName("Фильм");
		someFilm.setDescription("Описание фильма");
		someFilm.setReleaseDate(LocalDate.of(2003, 2, 3));
		someFilm.setDuration(-90);

		assertThrows(ValidationException.class, () -> filmService.createFilm(someFilm));

		assertFalse(filmService.getFilms().contains(someFilm));
	}
}
