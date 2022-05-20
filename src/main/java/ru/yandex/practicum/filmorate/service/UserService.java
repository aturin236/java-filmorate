package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
       this.userStorage = userStorage;
   }

   public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
   }

   public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
   }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Optional<User> getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        addFriendToUser(userId, friendId);
        addFriendToUser(friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        deleteFriendToUser(userId, friendId);
        deleteFriendToUser(friendId, userId);
    }

    public Collection<User> getFriends(Long userId) {
        checkUserAvailability(userId);

        return friends.getOrDefault(userId, new HashSet<>()).stream()
                .map(x -> userStorage.getUserById(x).get())
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        Set<Long> userFriends = friends.get(userId);
        if (userFriends == null) return new ArrayList<>();

        Set<Long> friendFriends = friends.get(friendId);
        if (friendFriends == null) return new ArrayList<>();

        Set<Long> resultFriends = new HashSet<>(userFriends);

        resultFriends.retainAll(friendFriends);

        return resultFriends.stream()
                .map(x -> userStorage.getUserById(x).get())
                .collect(Collectors.toList());
    }

    private void addFriendToUser(Long userId, Long friendId) {
        Set<Long> userFriends = friends.getOrDefault(userId, new HashSet<>());
        userFriends.add(friendId);
        friends.put(userId, userFriends);
    }

    private void deleteFriendToUser(Long userId, Long friendId) {
        Set<Long> userFriends = friends.get(userId);

        if (userFriends == null) return;

        userFriends.remove(friendId);
        friends.put(userId, userFriends);
    }

    private void checkUserAvailability(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id=%s", id));
        }
    }
}
