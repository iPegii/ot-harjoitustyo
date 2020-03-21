package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;
    Kassapaate kassapaate;
    
    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
        kassapaate = new Kassapaate();
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
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
    public void negatiivisenSaldonLataaminenKassapaatteellaEiToimi() {
        kassapaate.lataaRahaaKortille(kortti, -500);
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void saldonLataaminenToimiiOikeinKassapaatteella() {
        kassapaate.lataaRahaaKortille(kortti, 5000);
        assertEquals("saldo: 60.0", kortti.toString());
        assertTrue(105000 == kassapaate.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiToimiiOikein() {
       boolean toteutuu =  kassapaate.syoEdullisesti(kortti);
        assertEquals("saldo: 7.60", kortti.toString());
        assertTrue(toteutuu);
        assertTrue(1 == kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiToimiiOikein() {
        boolean toteutuu = kassapaate.syoMaukkaasti(kortti);
        assertEquals("saldo: 6.0", kortti.toString());
        assertTrue(toteutuu);
        assertTrue(1 == kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void saldoEiMeneNegatiiviseksiSyoEdullisesti() {
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        boolean toteutuuko =  kassapaate.syoEdullisesti(kortti);
        assertEquals("saldo: 2.0", kortti.toString());
        assertFalse(toteutuuko);
        assertTrue(0 == kassapaate.edullisiaLounaitaMyyty());
        assertTrue(100000 == kassapaate.kassassaRahaa());
    }
    
    @Test
    public void saldoEiMeneNegatiiviseksiSyoMaukkaasti() {
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        boolean toteutuuko =  kassapaate.syoMaukkaasti(kortti);
        assertEquals("saldo: 2.0", kortti.toString());
        assertFalse(toteutuuko);
        assertTrue(2 == kassapaate.maukkaitaLounaitaMyyty());
        assertTrue(100000 == kassapaate.kassassaRahaa());
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaOnTarpeeksi() {
        assertTrue(kortti.otaRahaa(500));
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaEiOleTarpeeksi() {
        assertFalse(kortti.otaRahaa(1200));
    }
    
    @Test
    public void kassapaatteenLuominenToimii() {
        assertTrue(100000 == kassapaate.kassassaRahaa());
        assertTrue(0 == kassapaate.maukkaitaLounaitaMyyty());
        assertTrue(0 == kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoToimiiEdullisesti() {
        int vaihtoraha = kassapaate.syoEdullisesti(260);
        assertTrue(vaihtoraha == 20);
        assertTrue(1 ==kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoToimiiMaukkaasti() {
        int vaihtoraha = kassapaate.syoMaukkaasti(460);
        assertTrue(vaihtoraha == 60);
        assertTrue(1 == kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoEdullisestiKunRahaaLiianVahan() {
        int vaihtoraha = kassapaate.syoEdullisesti(200);
        assertTrue(vaihtoraha == 200);
        assertTrue(0 == kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoMaukkaastiKunRahaaLiianVahan() {
        int vaihtoraha = kassapaate.syoMaukkaasti(390);
        assertTrue(vaihtoraha == 390);
        assertTrue(0 == kassapaate.maukkaitaLounaitaMyyty());
    }

}
