
package financetrackerapp.dao;

import financetrackerapp.domain.User;
import financetrackerapp.mongodb.UserService;
import java.util.List;

public interface UserDao {
    public User create(User user);
    public User findByUsername(String username);
    public List<User> getAll();
    public String[] getFileName();
  //  public void changeName(String username);
    public void delete(String username);
    public UserService getDatabase();
}
