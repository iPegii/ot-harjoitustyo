/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.domain;

import at.favre.lib.crypto.bcrypt.BCrypt;
import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.UserDao;
import financetrackerapp.mongodb.FinanceService;
import financetrackerapp.mongodb.UserService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.bson.Document;

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
 * @param password password of the user
 * 
 * @see financetrackerapp.domain.User

 *
 * @return User
 */
    public String login(String username, String password) {
        User userObject = userDao.findByUsername(username);
        if (userObject == null) {
            return "User not found";
        } else {
            if (checkPasswordHash(userObject, password) == true) {
                this.user = userObject;
                return "true";
            } else {
                return "Password was incorrect";
            }
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
 * @param password password of the user
 * 
 * @see financetrackerapp.mongodb.UserService#create(String,String,String)
 * 
 * @return luotu käyttäjä tai null
 */
    public User createUser(String username, String name, String password) {
        if (userDao.findByUsername(username) == null) {
            String passwordHash = createPasswordHash(password);
            UserService userService = userDao.getDatabase();
            User newUser = userService.create(username, name, passwordHash);
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
    
    
     /**
 * Method takes price and name, 
 * new User-object is created if unique
 * 
 * 
     * @param finance is the finance that replaces old one
     * @return Boolean returns true if update was succesful, otherwise
     * returns false
 * @see financetrackerapp.mongodb.FinanceService#updateFinance(String, Double, String, String, String)
 */
    public Boolean updateFinance(Finance finance) {
        Double newPrice = finance.getPrice();
        String id = finance.getId();
        String event = finance.getEvent();
        String date = finance.getDate();
        String userId = user.getId();
        FinanceService financeService = financeDao.getDatabase();
        Document result = financeService.updateFinance(id, newPrice, event, date, userId);
        
        if (result.get("_id").equals(id)) {
            financeDao.updateFinance(finance);
        }
        return result.get("_id").equals(id);
    }
    
    public Boolean deleteFinance(String id) {
        FinanceService financeService = financeDao.getDatabase();
        Boolean result = financeService.deleteFinance(id);
        if (result == true) {
            financeDao.deleteFinance(id);
            return true;
        }
        return false;
    }
    
    public List<Finance> getAllFinances() {
        if (user == null) {
            return null;
        } else {
            return financeDao.getAll().stream()
                .filter(finance -> finance.getUser().equals(user.getId()))
                .collect(Collectors.toList());
        }
    }
    
    public List<User> getAllUsers() {
        return userDao.getAll().stream()
            .collect(Collectors.toList());
    }
    
    public String getBalance() {
        double balance = 0;
        for (Finance f: getAllFinances()) {
            balance += f.getPrice();
        }
        String balanceFormatted = formatPrice(balance);
        return balanceFormatted;
    }
    
    
    public static String formatPrice(Double price) {
        Locale locale = new Locale("de", "DE");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        Currency eurCurrency = Currency.getInstance("EUR");
        decimalFormat.setCurrency(eurCurrency);
        String formattedPrice = decimalFormat.format(price) + " " + eurCurrency.getSymbol();
        return formattedPrice;
    }
    
    public static String createPasswordHash(String password) {
        String hash = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        return hash;
    }
    
    public static Boolean checkPasswordHash(User userToLogin, String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userToLogin.getPasswordHash());
        return result.verified;
    }
}