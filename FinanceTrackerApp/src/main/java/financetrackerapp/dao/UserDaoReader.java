
package financetrackerapp.dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import financetrackerapp.domain.User;
import financetrackerapp.mongodb.UserService;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDaoReader implements UserDao {
    private List<User> users;
    private String[] userSettings;
    private UserService userService;
    
    public UserDaoReader(String[] fileSettings, UserService userService) {
        this.users = new ArrayList<>();
        this.userSettings = fileSettings;
        this.userService = userService;
        try {
            read();
        } catch (IOException ex) {
            System.out.println("Error while initializing users file");
        }
    }
    
    public void init() {
        List<User> userList = userService.getAll();
        this.users = userList;
        save();
    }

    public void read() throws IOException {
        File file = new File(userSettings[0] + userSettings[1]);
        if (!file.exists()) {
            file.createNewFile();
            init();
        } else {
            try (FileReader reader = new FileReader(userSettings[0] + userSettings[1])) {
                Gson gson = new Gson();
                //CHECKSTYLE.OFF: WhitespaceAround - Curly braces need whitespaces, ignoring makes this more readable
                Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
                //CHECKSTYLE.ON: WhitespaceAround
                List<User> userList = gson.fromJson(reader, userListType);
                users.clear();
                if (userList != null) {
                    this.users = userList;
                }
                init();
            } catch (IOException e) {
                System.out.println("Error reading the users file: " + e);
            }
        }
    }
    public void save() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(userSettings[0] + userSettings[1]));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonTree = gson.toJsonTree(users);
            gson.toJson(jsonTree, writer);
            
            writer.close();
        } catch (JsonIOException e) {
            System.out.println("Gson unable to write users file: " + e);
        } catch (IOException e) {
            System.out.println("Error writing to the users file: " + e);
        }
    }
    
    public List<User> getAll() {
        return users;
    }
    
    public User findByUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }
    
    public User create(User user) {
        users.add(user);
        save();
        return user;
    }
    
    public void delete(String username) {
        User userToRemove = null;
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                userToRemove = user;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
        } else {
            System.out.println("User not found: " + username);
        }
        save();
    }
    
    public String[] getFileName() {
        return userSettings;
    }
    
    public UserService getDatabase() {
        return userService;
    }
}
