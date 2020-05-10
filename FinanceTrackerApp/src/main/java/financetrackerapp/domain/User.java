
package financetrackerapp.domain;

import java.util.Objects;


public class User {
    
    private String id;
    private String username;
    private String name;
    private String passwordHash;
    
    public User(String username, String name, String id, String passwordHash) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.passwordHash = passwordHash;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getId() {
        return this.id;
    }
    public String getPasswordHash() {
        return this.passwordHash;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", name=" + name + ", passwordHash=" + passwordHash + '}';
    }
}
