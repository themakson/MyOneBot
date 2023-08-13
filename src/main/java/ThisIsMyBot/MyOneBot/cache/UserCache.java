package ThisIsMyBot.MyOneBot.cahce;

import ThisIsMyBot.MyOneBot.entities.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserCache implements ThisIsMyBot.MyOneBot.cahce.Cache<User> {
    private final Map<Long, User> cacheOfAllUsers;

    public UserCache() {
        this.cacheOfAllUsers = new HashMap<>();
    }


    @Override
    public void add(User user) {
        cacheOfAllUsers.putIfAbsent(user.getId(), user);
    }

    @Override
    public void remove(Long id) {
        cacheOfAllUsers.remove(id);
    }

    @Override
    public User findBy(Long id) {
        return cacheOfAllUsers.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(cacheOfAllUsers.values());
    }
}

