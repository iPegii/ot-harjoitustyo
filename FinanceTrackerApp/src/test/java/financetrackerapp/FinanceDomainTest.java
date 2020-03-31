/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
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
public class FinanceDomainTest {
    @Before
    public void setUp() {
    }

    @Test
    public void testCreatingFinanceIsCorrect() {
        Finance finance = new Finance(50,"Special coffee", "22.02.2019");
        assertTrue(finance.getPrice() == 50);
        assertEquals(finance.getEvent(), "Special coffee");
        assertEquals(finance.getDate(), "22.02.2019");
    }
    
    @Test
    public void testFinanceToString() {
        Finance finance = new Finance(40,"Very nice tea", "23.02.2019");
        assertEquals(finance.toString(), "40;Very nice tea;23.02.2019");
    }
    
    @Test
    public void testCreatingUserIsCorrect() {
        User user = new User("gath20", "XxTrollerxX");
        assertEquals(user.getUsername(), "gath20");
        assertEquals(user.getName(), "XxTrollerxX");
    }
    
    @Test
    public void TestUserToString() {
       User user = new User("zack", "SuchDogeMuchMeme");
       assertEquals(user.toString(), "zack;SuchDogeMuchMeme");
    }
    

    
    
}
