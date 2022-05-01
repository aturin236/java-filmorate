package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
       this.userStorage = userStorage;
   }

    public void addFriend(Long userId, Long friendId) {
        addFriendToUser(userId, friendId);
        addFriendToUser(friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        deleteFriendToUser(userId, friendId);
        deleteFriendToUser(friendId, userId);
    }

    public Optional<Collection<Long>> getFriends(Long userId) {
        return Optional.ofNullable(friends.get(userId));
    }

    public Optional<Collection<Long>> getCommonFriends(Long userId, Long friendId) {
        Set<Long> userFriends = friends.get(userId);
        if (userFriends == null) return Optional.empty();

        Set<Long> friendFriends = friends.get(friendId);
        if (friendFriends == null) return Optional.empty();
        userFriends.retainAll(friendFriends);

        return Optional.of(userFriends);
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
}
