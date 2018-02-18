package UserManager;

import java.util.*;

public class UserManager {



    private final Map<String,String> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public void addUser(String username,String type) {
        usersMap.put(username,type);
    }

    public void removeUser(String username) {
        usersMap.remove(username);
    }

    public Map<String,String> getUsers() {
        return usersMap;
    }

    public boolean isUserExists(String username) { return usersMap.containsKey(username); }
}
