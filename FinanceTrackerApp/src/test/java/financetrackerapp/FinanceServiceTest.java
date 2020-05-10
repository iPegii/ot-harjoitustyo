/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import financetrackerapp.domain.Finance;
import financetrackerapp.mongodb.FinanceService;
import java.io.File;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iPegii
 */
public class FinanceServiceTest {
    
    FinanceService financeService;
    
    public FinanceServiceTest() {
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
        financeService.deleteAll();
    }
    
    @Before
    public void setUp() {
        this.financeService = new FinanceService("ohte-test");
    }
    
    @Test
    public void createWorks() {
        String id = new ObjectId().toString();
        Finance fcreated = financeService.create(30, "Nice test event", "2020-02-02", id);
        Finance fretrieved = financeService.getAll().get(0);
        assertEquals(fcreated.toString(), fretrieved.toString());
    }
    
    @Test
    public void deleteWorks() {
        String id = new ObjectId().toString();
        Finance fcreated = financeService.create(30, "Nice test event", "2020-02-02", id);
        Boolean removed = financeService.deleteFinance(fcreated.getId());
        assertTrue(removed);
        List<Finance> fList = financeService.getAll();
        assertTrue(fList.isEmpty());
    }
}
