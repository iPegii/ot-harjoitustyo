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
public class FinanceReaderTest {
    
    private FinanceDaoReader financeReader;
    private UserDaoReader userReader;
    
    private String[] financeFileSettings;
    private String[] userFileSettings;
    
    UserService userService;
    FinanceService financeService;
    
    private DaoService daoService;
    
    
    private File financeFile;
    private File userFile;

    
    public FinanceReaderTest() {
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
                System.out.println("Error cleaning financeTest: " + ex.getMessage());
            }
        }
        financeService.deleteAll();
        
        
        if(userFile.exists() && userFile.isFile()) {
            try(PrintWriter userClean = new PrintWriter(userFile)) {
            } catch (Exception ex) {
                System.out.println("Error cleaning userTest: " + ex.getMessage());
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
        String fileFolder = financeReader.getFileName()[0];
        String fileName = financeReader.getFileName()[1];
        String filePath = fileFolder + fileName;
        
        assertEquals(filePath, financeFileSettings[0]+financeFileSettings[1]);
        
        this.financeFile = new File(filePath);
        financeFile.delete();
        
        try {
            File file = new File(filePath);
            financeReader.read();
            assertTrue(file.exists());
        } catch (IOException ex) {
            System.out.println("Error reading finance file: " + ex.getMessage());
        }
    }
    
    @Test
    public void readerCreatesFinanceToListAndToFile() {
        String userId = new ObjectId().toString();
        String passwordHash = daoService.createPasswordHash(userId);
        User user = new User("Pegi", "Pegii", userId, passwordHash);
        userReader.create(user);
        String financeId = new ObjectId().toString();
        Finance finance = new Finance(financeId, 50, "test events are great", "2020-01-01", user.getId());
        financeReader.create(finance);
        
        String correctFinance = "Finance{" + "id=" + financeId + ", price=" + 50.0 + ","
                + " event=" + "test events are great" + ", date=" + "2020-01-01" + ", user=" + user.getId() + ","
                        + " formattedPrice=" + "50 €" + '}';
        
        Finance firstFromList = financeReader.getAll().get(0);
        assertEquals(correctFinance, firstFromList.toString());
        List<Finance> financeList = null;
        
        try(FileReader reader = new FileReader(financeFile)) {
            Gson gson = new Gson();
            //CHECKSTYLE.OFF: WhitespaceAround - Curly braces need whitespace, ignoring makes this more readable
            Type financeListType = new TypeToken<ArrayList<Finance>>(){}.getType();
            //CHECKSTYLE.ON: WhitespaceAround
            financeList = gson.fromJson(reader, financeListType);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        assertTrue(financeList != null);
        assertEquals("Finance{" + "id="
                    + financeId + ", price=" + 50.0 + ", event=" + "test events are great" + ","
                    + " date=" + "2020-01-01" + ", user=" + user.getId() + ", formattedPrice=" + "50 €" +
                    '}', financeList.get(0).toString());
     
    }
    
    @Test
    public void ifJsonFileIsNullThenFileIsNotCopied() {
        String userId = new ObjectId().toString();
        String passwordHash = daoService.createPasswordHash("Th1s1sV3r7G00d");
        User user = new User("Pegi", "Pegii", userId, passwordHash);
        userReader.create(user);
        String financeId = new ObjectId().toString();
        Finance finance = new Finance(financeId, 50, "test events are great", "2020-01-01", user.getId());
        financeReader.create(finance);
        try {
            financeReader.read();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        assertTrue(financeReader.getAll() != null);
    }
}
