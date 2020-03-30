
package financetrackerapp.domain;

public class User {
    
    private String name;
    private String username;
    
    public User(String username,String name) {
        this.name = name;
        this.username = username;
    //    this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    public String getUsername() {
        return this.username;
    }
    public String toString() {
        return "name: " + name + ", username: " + username;
    }
    
    
}
