/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp;

import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iPegii
 */

public class ObjectTest {

    @Test
    public void testCreatingFinanceIsCorrect() {
        String id = new ObjectId().toString();
        String userId = new ObjectId().toString();
        Finance finance = new Finance(id,50,"Special coffee", "2019-02-20", userId);
        assertTrue(finance.getId().equals(id));
        assertTrue(finance.getPrice() == 50);
        assertEquals(finance.getEvent(), "Special coffee");
        assertEquals(finance.getDate(), "2019-02-20");
        assertTrue(finance.getUser().equals(userId));
        assertEquals(finance.getFormattedPrice(), "50 €");
    }
    
    @Test
    public void testFinanceToString() {
        String id = new ObjectId().toString();
        String userId = new ObjectId().toString();
        Finance finance = new Finance(id, 40,"Very nice tea", "2019-02-23", userId);
        String goodToString = "Finance{" + "id=" + id + ","
                + " price=" + "40.0" + ", event=" + "Very nice tea" + ","
                + " date=" + "2019-02-23" + ", user=" + userId + ","
                + " formattedPrice=" + "40 €" + '}';
        assertEquals(finance.toString(), goodToString);
    }
    
    @Test
    public void testCreatingUserIsCorrect() {
        String userId = new ObjectId().toString();
        String passwordHash = "sdfgojgfdfsug.42245dada";
        User user = new User("gath20", "XxTrollerxX", userId, passwordHash);
        assertEquals(user.getUsername(), "gath20");
        assertEquals(user.getName(), "XxTrollerxX");
        assertEquals(user.getId(), userId);
        assertEquals(user.getPasswordHash(), passwordHash);
    }
    
}
