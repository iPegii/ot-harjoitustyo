/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.domain.Finance;
import java.io.File;
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
    
    private FinanceDaoReader reader;
    private String fileName;
    
    public FinanceDaoTest() {
    }
    
    @Before
    public void setUp() {
        this.fileName = "testFinance.txt";
        File file = new File(fileName);
        file.delete();
        this.reader = new FinanceDaoReader(fileName);
    }
    
    @Test
    public void fileIsCreatedIfItDoesNotExist() {
        File file = new File(fileName);
        reader.read();
        assertTrue(file.exists());
    }
    
    @Test
    public void readerCreatesFinanceToListAndToFile() {
        reader.create(new Finance("Pegi", 50, "Test event", "23.10.2020"));
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
