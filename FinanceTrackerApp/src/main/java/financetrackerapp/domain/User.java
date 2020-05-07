
package financetrackerapp.domain;

import javafx.scene.control.Button;


public class User {
    
    private String id;
    private String username;
    private String name;
    private String passwordHash;
    private transient Button removeButton;
    private transient Button modifyButton;
    
    public User(String username, String name, String id, String passwordHash) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.passwordHash = passwordHash;
        this.removeButton = new Button("Remove user");
        this.modifyButton = new Button("Change name");
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
