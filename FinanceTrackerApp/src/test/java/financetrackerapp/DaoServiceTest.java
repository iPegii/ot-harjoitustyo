/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.dao.UserDao;
import financetrackerapp.dao.UserDaoReader;
import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import financetrackerapp.mongodb.FinanceService;
import financetrackerapp.mongodb.UserService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iPegii
 */

public class DaoServiceTest {
    
    private FinanceDaoReader financeReader;
    private UserDaoReader userReader;
    
    private String[] financeFileSettings;
    private String[] userFileSettings;
    
    UserService userService;
    FinanceService financeService;
    
    
    private File financeFile;
    private File userFile;
    
    private DaoService daoService;
    
    
    public DaoServiceTest() {
    }
    @BeforeClass
    public static void testInitializing() {
        File folder = new File("testResources");
            if(!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
    }
    
    @After
    public void cleanupAfterTests() {
        if(financeFile.exists()) {
        try(PrintWriter financeClean = new PrintWriter(financeFile)) {
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        financeService.deleteAll();
        if(userFile.exists() && userFile.isFile()) {
            try(PrintWriter userClean = new PrintWriter(userFile)) {
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        userService.deleteAll();
    }
    
    @Before
    public void setUp() {
        this.financeFileSettings = new String[]{"testResources/", "testFinances.json"};
        this.userFileSettings = new String[]{"testResources/", "testUsers.json"};
        
        this.userService = new UserService("ohte-test");
        this.financeService = new FinanceService("ohte-test");
        
        
        this.financeReader = new FinanceDaoReader(financeFileSettings, financeService);
        this.userReader = new UserDaoReader(userFileSettings, userService);

        this.financeFile = new File(financeFileSettings[0]+financeFileSettings[1]);
        this.userFile = new File(userFileSettings[0]+userFileSettings[1]);
        
        FinanceDao financeDao= new FinanceDaoReader(financeFileSettings, financeService);
        UserDao userDao = new UserDaoReader(userFileSettings, userService);
        this.daoService = new DaoService(userDao, financeDao);
    }
    
    @Test
    public void correctNumberOfFinancesStored() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        daoService.login(username, password);
        String id = daoService.loggedIn().getId();
        daoService.createFinance(50, "Great test event 1", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 2", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 3", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 4", "2020.04.27", id);
        
        assertTrue("correct number of finances not created" + daoService.getAllFinances().size(), daoService.getAllFinances().size() == 4);
        
        FinanceDao financeTester= new FinanceDaoReader(financeFileSettings, financeService);
        UserDao userTester = new UserDaoReader(userFileSettings, userService);
        DaoService duplicateTester = new DaoService(userTester, financeTester);
        duplicateTester.login(username, password);
        
        assertTrue("finances duplicated on launch",duplicateTester.getAllFinances().size() == 4);
    }
    
    @Test
    public void updatingFinanceWorks() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        daoService.login(username, password);
        String id = daoService.loggedIn().getId();
        daoService.createFinance(10, "Great test event 1", "2020-03-03", id);
        daoService.createFinance(20, "Great test event 2", "2020-04-04", id);
        daoService.createFinance(30, "Great test event 3", "2020-05-05", id);
        
        List<Finance> list = daoService.getAllFinances();
        Finance finance = list.get(1);
        assertTrue(finance.getPrice() == 20.0);
        assertEquals(finance.getEvent(), "Great test event 2");
        assertEquals(finance.getDate(), "2020-04-04");
        assertEquals(finance.getUser(), id);
        
        String fId = finance.getId();
        Double fPrice = 1000.0;
        String fEvent = "Updated event works";
        String fDate = "1995-05-07";
        String fUser = finance.getUser();
        
        
        Finance updatedFinance = new Finance(fId, fPrice, fEvent, fDate, fUser);
        
        daoService.updateFinance(updatedFinance);
        
        List<Finance> updatedList = daoService.getAllFinances();
        Finance newFinance = null;
        for(Finance uf: updatedList) {
            if(uf.getId().equals(fId)) {
                newFinance = uf;
            }
        }
        
        assertEquals(newFinance, updatedFinance);
        
        FinanceDao financeTester= new FinanceDaoReader(financeFileSettings, financeService);
        UserDao userTester = new UserDaoReader(userFileSettings, userService);
        DaoService updateSavedTester = new DaoService(userTester, financeTester);
        updateSavedTester.login(username, password);
        
        List<Finance> restartUpdateList = daoService.getAllFinances();
        Finance restartNewFinance = finance;
        for(Finance uf: restartUpdateList) {
            if(uf.getId().equals(fId)) {
                restartNewFinance = uf;
            }
        }
        
        assertEquals(updatedFinance.toString(), restartNewFinance.toString());
    }
    
    @Test
    public void deletingFinanceWorks() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        daoService.login(username, password);
        String id = daoService.loggedIn().getId();
        daoService.createFinance(50, "Great test event 1", "2020-04-27", id);
        daoService.createFinance(100, "Great test event 2", "2020-04-28", id);
        daoService.createFinance(150, "Great test event 3", "2020-05-06", id);
        
        List<Finance> list = daoService.getAllFinances();
        Finance finance = list.get(1);
        
        assertTrue(list.size() == 3);
        
        String fId = finance.getId();
        
        daoService.deleteFinance(fId);
        
        List<Finance> newList = daoService.getAllFinances();
        
        assertTrue(newList.size() == 2);
        
        Finance finance1 = newList.get(0);
        Finance finance2 = newList.get(1);
        
        assertEquals(finance1.getEvent(), "Great test event 1");
        assertEquals(finance2.getEvent(), "Great test event 3");
    }
    
    @Test
    public void usersAreCreatedAndRetrievedCorrectly() {
        String username1 = "Agi";
        String name1 = "User1";
        String password1 = "Th1s1sV3r7G00d";
        User result1 = daoService.createUser(username1, name1, password1);
        String username2 = "Bgi";
        String name2 = "User2";
        String password2 = "Th1s1sV3r7G00d";
        User result2 = daoService.createUser(username2, name2, password2);
        String username3 = "Agi";
        String name3 = "User3";
        String password3 = "Th1s1sV3r7G00d";
        User result3 = daoService.createUser(username3, name3, password3);
        String username4 = "Dgi";
        String name4 = "User1";
        String password4 = "Th1s1sV3r7G00d";
        User result4 = daoService.createUser(username4, name4, password4);
        
        assertTrue(result1 != null);
        assertTrue(result2 != null);
        assertTrue(result3 == null);
        assertTrue(result4 != null);
        
        List<User> userList = daoService.getAllUsers();
        
        assertTrue(userList.size() == 3);
        
        assertEquals(userList.get(2), result4);
    }
    
    
    @Test 
    public void getBalanceIsCorrect() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        daoService.login(username, password);
        String id = daoService.loggedIn().getId();
        daoService.createFinance(1000, "Great test event 1", "2020-04-27", id);
        daoService.createFinance(2000, "Great test event 2", "2020-04-28", id);
        daoService.createFinance(3000, "Great test event 3", "2020-05-06", id);
        
        
        
        List<Finance> financeList = daoService.getAllFinances();
        Double balance = 0.0;
        for(Finance f: financeList) {
            balance += f.getPrice();
        }
        String balanceFormatted = DaoService.formatPrice(balance);
        assertEquals(balanceFormatted, daoService.getBalance());
        assertEquals("6,000 €",daoService.getBalance());
    }
    
    @Test
    public void wrongPasswordDoesNotWork() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        String fakeUsername = "Pegi";
        String fakePassword = "ThisIsVeryGood";
        String result = daoService.login(fakeUsername, fakePassword);
        assertEquals(result, "Password was incorrect");
        assertTrue(daoService.loggedIn() == null);
    }
    
    @Test
    public void wrongUsernameReturnsCorrectMessage() {
        String username = "Pegi";
        String name = "Pegii";
        String password = "Th1s1sV3r7G00d";
        daoService.createUser(username, name, password);
        String fakeUsername = "Peegi";
        String fakePassword = "Th1s1sV3r7G00d";
        String result = daoService.login(fakeUsername, fakePassword);
        assertEquals(result, "User not found");
        assertTrue(daoService.loggedIn() == null);
    }

}
