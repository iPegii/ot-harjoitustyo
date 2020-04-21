/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.dao.UserDaoReader;
import financetrackerapp.domain.Finance;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class FinanceDaoTest {
    
    private FinanceDaoReader financeReader;
    private UserDaoReader userReader;
    private String financeFileName;
    private String userFileName;
    
    public FinanceDaoTest() {
    }
    
    @Before
    public void setUp() {
        File folder = new File("testResources");
            if(!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
        this.financeFileName = "testResources/testFinances.json";
        this.userFileName = "testResources/testUsers.json";
        File financeFile = new File(financeFileName);
        File userFile = new File(userFileName);
        financeFile.delete();
        userFile.delete();
        this.financeReader = new FinanceDaoReader(financeFileName);
        this.userReader = new UserDaoReader(userFileName);
    }
    
    @Test
    public void fileIsCreatedIfItDoesNotExist() {
        try {
            File file = new File(financeFileName);
            financeReader.read();
            assertTrue(file.exists());
        } catch (IOException ex) {
            System.out.println("Error reading finance file: " + ex.getMessage());
        }
    }
    
    @Test
    public void readerCreatesFinanceToListAndToFile() {
        financeReader.create(new Finance("Pegi", 50, "Test event", "23.10.2020"));
        Finance listTest = reader.getAll().get(0);
        assertEquals(listTest.toString(), "Pegi;50;Test event;23.10.2020");
        reader.read();
        Finance fileTest = reader.getAll().get(0);
        assertEquals(fileTest.toString(), "Pegi;50;Test event;23.10.2020");
    }
    
    @Test
    public void fileCanBeWrittenOn() {
        reader.create(new Finance("Pegi", 50, "Test event", "23.10.2020"));
        Finance test = reader.getAll().get(0);
        assertEquals(test.toString(), "Pegi;50;Test event;23.10.2020");
    }


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
