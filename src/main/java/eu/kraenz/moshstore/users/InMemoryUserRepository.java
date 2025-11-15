package eu.kraenz.moshstore.users;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class InMemoryUserRepository implements UserRepository {
    private HashMap<String, User> users = new HashMap();

    @Override
    public void save(User user) {
        if(users.containsKey(user.email.toLowerCase())){
            throw new RuntimeException("User with this email already exists.");
        }
        users.put(user.email.toLowerCase(), user);
        System.out.println("User saved. id: "+ user.id);
    }
}
