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
    
    public String createFinance(double price, String event, String date, String userId) {
        FinanceService financeService = financeDao.getDatabase();
        Finance newFinance = financeService.create(price, event, date, userId);
        if (newFinance != null) {
            String response = financeDao.create(newFinance);
            return response;
        }
        return "Couldn't add this";   
    }
    
    public void updateFinance(Finance finance) {
        String newPrice = String.valueOf(finance.getPrice());
        String id = finance.getId();
        String event = finance.getEvent();
        String date = finance.getDate();
        String userId = user.getId();
        FinanceService financeService = financeDao.getDatabase();
        financeService.updateFinance(id, newPrice, event, date, userId);
    }
    
    public void updateUser() {
        String userId = user.getId();
        String username = user.getUsername();
        UserService userService = userDao.getDatabase();
        userService.updateUser(userId, username);
    }
    
    public List<Finance> getAll() {
        if (user == null) {
            return null;
        } else {
            return financeDao.getAll().stream()
                .filter(finance -> finance.getUser().equals(user.getId()))
                .collect(Collectors.toList());
        }
    }
    
    public double getBalance() {
        double balance = 0;
        for (Finance f: getAll()) {
            balance += f.getPrice();
        }
        return balance;
    }
}