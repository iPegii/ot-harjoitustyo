
package dao;

import domain.User;
import java.util.List;

public interface UserDao {
    public User create(User user);
    public User findByUsername(String username);
    public List<User> getAll();
    public void changeName(String username);
    public void delete(String username);
}
