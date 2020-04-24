
package financetrackerapp.domain;


public class User {
    
    private String name;
    private String username;
    private String _id;
    
    public User(String username, String name, String id) {
        this.name = name;
        this.username = username;
        this._id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getId() {
        return this._id;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", username=" + username + ", id=" + _id + '}';
    }
    
}
