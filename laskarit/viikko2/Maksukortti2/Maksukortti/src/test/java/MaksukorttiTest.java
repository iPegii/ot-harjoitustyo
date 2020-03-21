/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author makipekk
 */
public class MaksukorttiTest {
    
    public MaksukorttiTest() {
    }
    Maksukortti kortti;
    Kassapaate kassapaate;
            
    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
        kassapaate = new Kassapaate();
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void saldonLataaminenToimiiOikein() {
        kortti.lataaRahaa(500);
        assertEquals("saldo: 15.0", kortti.toString());
    }
    
    @Test
    public void syoEdullisestiToimiiOikein() {
        kassapaate.syoEdullisesti(kortti);
        assertEquals("saldo: 7.60", kortti.toString());
    }
    
    @Test
    public void syoMaukkaastiToimiiOikein() {
        kassapaate.syoMaukkaasti(kortti);
        assertEquals("saldo: 6.0", kortti.toString());
    }
    
    @Test
    public void saldoEiMeneNegatiiviseksiSyoEdullisesti() {
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoEdullisesti(kortti);
        assertEquals("saldo: 2.0", kortti.toString());
    }
    
    @Test
    public void saldoEiMeneNegatiiviseksiSyoMaukkaasti() {
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals("saldo: 2.0", kortti.toString());
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaOnTarpeeksi() {
        assertTrue(kortti.otaRahaa(500));
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaEiOleTarpeeksi() {
        assertFalse(kortti.otaRahaa(1200));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
