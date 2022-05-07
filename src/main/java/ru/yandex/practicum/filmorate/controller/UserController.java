package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) throws ValidationException {
        log.debug("Запрос на добавление пользователя - {}", userDTO.getLogin());

        User user = userService.addUser(UserDTO.UserDTOToUser(userDTO));

        return UserDTO.UserToUserDTO(user);
    }

    @PutMapping
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) throws ValidationException {
        log.debug("Запрос на обновление пользователя - {}", userDTO.getLogin());

        User user = userService.updateUser(UserDTO.UserDTOToUser(userDTO));

        return UserDTO.UserToUserDTO(user);
    }

    @GetMapping
    public Collection<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserDTO::UserToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id=%s", id));
        }
        return UserDTO.UserToUserDTO(user.get());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDTO> getUserFriends(@PathVariable Long id) {
        return userService.getFriends(id).stream()
                .map(UserDTO::UserToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDTO> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId).stream()
                .map(UserDTO::UserToUserDTO)
                .collect(Collectors.toList());
    }
}
