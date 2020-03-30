
package financetrackerapp.dao;

import financetrackerapp.domain.User;
import java.io.*;
import java.util.*;

public class UserDaoService {
    
    private List<User> users;
    private String file;
    public UserDaoService(String file) {
        this.users = new ArrayList<>();
        this.file = file;
        read();
    }
    
    public void read() {
        try {
            File file = new File("users.txt");
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()) {
                String[] parts = myReader.nextLine().split(";");
                User u = new User(parts[1], parts[2]);
                users.add(u);
            }
            myReader.close();
        } catch(Exception e) {
            System.out.println("Error reading the file: " + e);
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            FileWriter writer = new FileWriter(new File(file));
            for(User user: users) {
                writer.write(user.getUsername()+";"+user.getName());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to the file: " + e);
            e.printStackTrace();
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
        for(User user: users) {
            if(user.getUsername().equals(username)) {
              userToRemove = user;
              break;
            }
        }
        if(userToRemove != null) {
            users.remove(userToRemove);
        } else {
            System.out.println("User not found: " + username);
        }
        save();
    }
    
}
