
# Testausdokumentti

Ohjelman testaus on toteutettu integraatio- ja yksikkötestauksella käyttämällä JUnit:ia, tämän lisäksi on tehty manuaalista järjestelmätestausta käynnistämällä ohjelma.

## Integraatiotestaus

### Sovelluslogiikka
Integraatiotestausta on tehty testausluokkaan [DaoServiceTest](/FinanceTrackerApp/src/test/java/financetrackerapp/DaoServiceTest.java), joka testaa käyttöliittymän käyttämää [DaoService](/FinanceTrackerApp/src/main/java/financetrackerapp/domain/DaoService.java)-luokkaa. Yksinkertaisia yksikkötestauksia on myös tehty [Finance](/FinanceTrackerApp/src/main/java/financetrackerapp/domain/Finance.java)- ja [User](/FinanceTrackerApp/src/main/java/financetrackerapp/domain/User.java)-olioille, nämä testit löytyvät testiluokasta [ObjectTest](/FinanceTrackerApp/src/test/java/financetrackerapp/ObjectTest.java).

### DAO-luokat

DAO-tallennusta on testattu yksikkötestauksella, testejä varten on luotu erikseen kansio nimeltään "testResources" jonka sisällä on saman nimiset tiedostot kuin oikeassa sovelluksessa. Testien jälkeen tiedostot tyhjennetään.  
  
Yksikkötestausta on tehty [FinanceDaoReader](/FinanceTrackerApp/src/main/java/financetrackerapp/dao/FinanceDaoReader.java)-luokalle, jonka toimintaan kuuluu Finance-olioiden tallennus tiedostoon. Testaus on suoritettu [FinanceReaderTest](/FinanceTrackerApp/src/test/java/financetrackerapp/FinanceReaderTest.java)-luokkaan.

### Pilvitallennus

Pilvitallennuksen testaus on toteutettu yksikkötestauksella. Testauksen konfigurointi on toteutettu siten, että MongoDB:ssä on samassa klusterissa kuin "ohte"-tietokanta niin "ohte-test"-tietokanta, johon testit tehdään. Jokaisen testin jälkeen tietokanta tyhjennetään.

Luokkien [FinanceService](/FinanceTrackerApp/src/main/java/financetrackerapp/mongodb/FinanceService.java) ja [UserService](/FinanceTrackerApp/src/main/java/financetrackerapp/mongodb/UserService.java) testit on tehty testiluokkiin: [FinanceServiceTest](/FinanceTrackerApp/src/test/java/financetrackerapp/FinanceServiceTest.java) ja [UserServiceTest](/FinanceTrackerApp/src/test/java/financetrackerapp/UserServiceTest.java). Testeissä on testattu, että olioiden lisääminen tietokantaan onnistuu ja Finance-olion poisto onnistuu.

### Testauskattavuus

Kun käyttöliittymä on jätetty testikattavuusraportin ulkopuolelle niin koko sovelluksen testikattavuudessa rivikattavuus on 86% tasolla ja haarautumiskattavuuden taso on 79%.

![Testauskattavuus](/FinanceTrackerApp/dokumentaatio/kuvat/testikattavuus-1.2.png)  

Testauksen ulkopuolelle on jäänyt monia try-catch-lohkoja, joissa tapahtuisi esimerkiksi I/O-operaatio epäonnistuu tai keskeytyy tallennuksissa.


### Järjestelmätestaus



