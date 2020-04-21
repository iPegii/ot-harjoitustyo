/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.domain;

import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.UserDao;
import financetrackerapp.mongodb.FinanceService;
import financetrackerapp.mongodb.UserService;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author iPegii
 */
public class DaoService {
    private UserDao userDao;
    private FinanceDao financeDao;
    private User user;
    public DaoService(UserDao userDao, FinanceDao financeDao) {
        this.userDao = userDao;
        this.financeDao = financeDao;
    }
    
    public User login(String username) {
        User userObject = userDao.findByUsername(username);
        if (userObject == null) {
            return null;
        } else {
            this.user = userObject;
            return userObject;
        }
    }
    public User loggedIn() {
        if (user == null) {
            return null;
        } else {
            return user;
        }
    }
    
    public void createUser(String username, String name) {
        // index[0] = "resources/" && index[1] == "users"
        UserService userService = userDao.getDatabase();
        User newUser = userService.create(username, name);
        userDao.create(newUser);
    }
    
    public String createFinance(int price, String event, String date, String userId) {
        FinanceService financeService = financeDao.getDatabase();
        Finance newFinance = financeService.create(price, event, date, userId);
        if (newFinance != null) {
            String response = financeDao.create(newFinance);
            return response;
        }
        return "Couldn't add this";   
    }
    
    public List<Finance> getAll() {
        if (user == null) {
            return null;
        } else {
            return financeDao.getAll().stream()
                .filter(finance -> finance.getUserId() == user.getId())
                .collect(Collectors.toList());
        }
    }
}