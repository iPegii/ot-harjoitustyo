/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.domain;

import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.UserDao;
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
    
    public void createUser(User user) {
        userDao.create(user);
    }
    
    public String createFinance(Finance finance) {
        String response = financeDao.create(finance);
        return response;
    }
    
    public List<Finance> getAll() {
        if (user == null) {
            return null;
        } else {
            return financeDao.getAll().stream()
                .filter(finance -> finance.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
        }
    }
}