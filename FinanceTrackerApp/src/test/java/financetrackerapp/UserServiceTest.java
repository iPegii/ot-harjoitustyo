/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

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
import java.io.PrintWriter;
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
public class UserServiceTest {
    
    UserService userService;
    
    public UserServiceTest() {
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
        userService.deleteAll();
    }
    
    @Before
    public void setUp() {
        this.userService = new UserService("ohte-test");
    }
    
    
    @Test
    public void createWorks() {
        String username = "username";
        String name = "name";
        String passwordHash = DaoService.createPasswordHash("GreatPassword");
        User ucreated = userService.create(username, name, passwordHash);
        User uretrieved = userService.getAll().get(0);
        assertEquals(ucreated.toString(), uretrieved.toString());
    }
    
}
