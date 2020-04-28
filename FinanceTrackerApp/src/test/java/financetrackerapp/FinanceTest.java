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

public class FinanceTest {
    
    private FinanceDaoReader financeReader;
    private UserDaoReader userReader;
    
    private String[] financeFileSettings;
    private String[] userFileSettings;
    
    UserService userService;
    FinanceService financeService;
    
    
    private File financeFile;
    private File userFile;
    
    private DaoService daoService;
    
    
    public FinanceTest() {
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
    public void fileIsCreatedIfItDoesNotExist() {
        this.financeFile = new File(financeFileSettings[0]+financeFileSettings[1]);
        financeFile.delete();
        
        try {
            File file = new File(financeFileSettings[0]+financeFileSettings[1]);
            financeReader.read();
            assertTrue(file.exists());
        } catch (IOException ex) {
            System.out.println("Error reading finance file: " + ex.getMessage());
        }
    }
    
    @Test
    public void readerCreatesFinanceToListAndToFile() {
        String userId = new ObjectId().toString();
        User user = new User("Pegi", "Pegii", userId);
        userReader.create(user);
        String financeId = new ObjectId().toString();
        Finance finance = new Finance(financeId, 50, "test events are great", "01.01.2020", user.getId());
        financeReader.create(finance);
        
        Finance firstFromList = financeReader.getAll().get(0);
        assertEquals(firstFromList.toString(),"Finance{" + "date=" + "01.01.2020" + ", event=" + "test events are great" + ","
                + " price=" + "50.0" + ", id=" + financeId + ", user=" + userId + '}');
        
        try(FileReader reader = new FileReader(financeFile)) {
            Gson gson = new Gson();
            //CHECKSTYLE.OFF: WhitespaceAround - Curly braces need whitespace, ignoring makes this more readable
            Type financeListType = new TypeToken<ArrayList<Finance>>(){}.getType();
            //CHECKSTYLE.ON: WhitespaceAround
            List<Finance> financeList = gson.fromJson(reader, financeListType);
            assertEquals(financeList.get(0).toString(), "Finance{" + "date=" + "01.01.2020" + ", event=" + "test events are great" + ","
                + " price=" + "50.0" + ", id=" + financeId + ", user=" + userId + '}');
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
     
    }
    
    @Test
    public void ifJsonFileIsNullThenFileIsNotCopied() {
    String userId = new ObjectId().toString();
    User user = new User("Pegi", "Pegii", userId);
    userReader.create(user);
    String financeId = new ObjectId().toString();
    Finance finance = new Finance(financeId, 50, "test events are great", "01.01.2020", user.getId());
    financeReader.create(finance);
        try {
            financeReader.read();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    assertTrue(financeReader.getAll() != null);
        
    }
    
    @Test
    public void correctNumberOfFinancesStored() {
        String username = "Pegi";
        String name = "Pegii";
        daoService.createUser(username, name);
        daoService.login("Pegi");
        String id = daoService.loggedIn().getId();
        daoService.createFinance(50, "Great test event 1", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 2", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 3", "2020.04.27", id);
        daoService.createFinance(50, "Great test event 4", "2020.04.27", id);
        
        assertTrue("correct number of finances not created", daoService.getAll().size() == 4);
        
        FinanceDao financeTester= new FinanceDaoReader(financeFileSettings, financeService);
        UserDao userTester = new UserDaoReader(userFileSettings, userService);
        DaoService duplicateTester = new DaoService(userTester, financeTester);
        duplicateTester.login(username);
        
        assertTrue("finances duplicated on launch",duplicateTester.getAll().size() == 4);
    }
    /*
    @Test
    public void fileCanBeWrittenOn() {
        financeReader.create(new Finance("Pegi", 50, "Test event", "23.10.2020"));
        Finance test = financeReader.getAll().get(0);
        assertEquals(test.toString(), "Pegi;50;Test event;23.10.2020");
    }
*/

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

}
