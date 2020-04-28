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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
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
    
    
 /**
 * Method takes username as parameter, logs user in and returns
 * the User-object logged in. 
 * 
 * @param username Username of the user
 * 
 * @see financetrackerapp.domain.User

 *
 * @return User
 */
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
/**
 * Method takes username and name, 
 * new User-object is created if unique
 * 
 * @param username Username of the user
 * @param name Nickname for the user
 * 
 * @see financetrackerapp.mongodb.UserService#create(String,String)
 * 
 * @return luotu käyttäjä tai null
 */
    public User createUser(String username, String name) {
        if (userDao.findByUsername(username) == null) {
            UserService userService = userDao.getDatabase();
            User newUser = userService.create(username, name);
            return userDao.create(newUser);
        }
        return null;
    }
    
    
 /**
 * Method takes price and name, 
 * new User-object is created if unique
 * 
 * @param price value of the event done
     * @param event description of the event
     * @param date  time of the event
     * @param userId user who made event in question
     * @return Was the operation succesful
 * 
 * @see financetrackerapp.mongodb.FinanceService#create(double, String , String , String )
 */
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
    
    public String getBalance() {
        double balance = 0;
        for (Finance f: getAll()) {
            balance += f.getPrice();
        }
        String balanceFormatted = formatPrice(balance);
        return balanceFormatted;
    }
    public static String formatPrice(Double price) {
        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        String formattedPrice = decimalFormat.format(price);
        return formattedPrice;
    }
}