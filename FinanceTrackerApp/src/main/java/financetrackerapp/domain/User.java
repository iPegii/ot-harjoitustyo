
package financetrackerapp.domain;


public class User {
    
    private String id;
    private String username;
    private String name;
    
    public User(String username, String name, String id) {
        this.id = id;
        this.username = username;
        this.name = name;
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

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", username=" + username + ", id=" + id + '}';
    }
    
}
