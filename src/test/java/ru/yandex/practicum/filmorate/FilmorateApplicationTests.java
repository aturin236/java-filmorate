package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPAA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorageDAO;
import ru.yandex.practicum.filmorate.storage.dao.UserStorageDAO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserStorageDAO userStorageDAO;
	private final FilmStorageDAO filmStorageDAO;
	private final JdbcTemplate jdbcTemplate;

	@Test
	public void testUserStorageDAO() {
		User user = new User("user", "user", 1, "user@user.com", LocalDate.now());

		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Users"), is(0));
		userStorageDAO.addUser(user);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Users"), is(1));

		user.setName("newUser");
		userStorageDAO.updateUser(user);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Users"), is(1));

		Optional<User> userFromDB = userStorageDAO.getUserById(1L);
		assertTrue(userFromDB.isPresent());
		assertSame("newUser", userFromDB.get().getName());

		Collection<User> allUsers = userStorageDAO.getAllUsers();
		assertThat(allUsers.size(), is(1));
	}

	@Test
	public void testFilmStorageDAO() {
		Film film = new Film("film", 1, "film",
				LocalDate.of(2000,5,12),
				(short) 120,
				new RatingMPAA(1, "rating", "rating"));

		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Films"), is(0));
		filmStorageDAO.addFilm(film);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Films"), is(1));

		film.setName("newFilm");
		filmStorageDAO.updateFilm(film);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "Films"), is(1));

		Optional<Film> filmFromDB = filmStorageDAO.getFilmById(1L);
		assertTrue(filmFromDB.isPresent());
		assertSame("newFilm", filmFromDB.get().getName());

		Collection<Film> allFilms = filmStorageDAO.getAllFilms();
		assertThat(allFilms.size(), is(1));
	}
}
