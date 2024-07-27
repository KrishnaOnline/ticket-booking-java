package ticketbooking.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticketbooking.models.User;

public class UserService {
    private User user;

    private List<User> userList;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERSDB_PATH = "app/src/main/java/ticketbooking/db/users.json";

    public UserService(User usr) throws IOException {
        this.user = usr;
        File users = new File(USERSDB_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser() {
        User userExists = userList.stream().filter(usr -> {
            return usr.getName().equals(user.getName());
        }).findFirst().orElse(null);
        return true;
    }
}