
# Käyttöohje


## Konfigurointi

Laita sovelluksen juuritiedostoon tiedosto keys.properties kuten kuvassa.
![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part1.png)  

config.properties tiedostossa voi muuttaa tiedostojen nimiä, johon tiedot tallenneteaan. Sisältö on valmiiksi toimiva: 

```
userFile=users.json   
financeFile=finances.json

```

keys.properties tiedoston sisältö riippuu käyttämästäsi MongoDB-klusterista, mutta lisäämällä seuraavaan esimerkkitekstin ja korjaamalla sulut omilla tiedoilla tietokanta toimii:
```
mongodb.uri=mongodb+srv://(kayttajatunnus):(salasana)@(Klusterin nimi)/test?retryWrites=true&w=majority
```

## Käyttäjän luominen
 
Sovelluksen käynnistyessä tulee kirjautumisruutu eteen, ensimmäisellä käynnistyksellä ei kuitenkaan ole käyttäjiä joten käyttäjä pitää luoda, tämä onnistuu 'create'-napilla  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part2.png) 

Käyttäjää luodessa tehdään itselle käyttäjänimi, kutsumanimi ja salasana. Kutsumanimi on näkyvillä yleisnäkymässä.  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part3.png) 

Kun käyttäjän luominen on onnistunut, painetaan 'cancel'-nappia ja tämän jälkeen voidaan kirjautua sisään juuri luomallemme käyttäjälle.  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part4.png) 

Nyt näytölle tulee sovelluksen päänäkymä, jossa näkyy tehdyt tapahtumat ja niiden tapahtumien hinnan summa. Vasemmassa yläkulmassa näkyvästä lomakkeesta voi lisätä uusia tapahtumia, 'clear'-napilla voidaan tyhjentää hinnan ja tapahtuman kentät. Päivämäärän pystyy valita 'clear'-napin yläpuolelta olevasta kalenterin-kuvakkeesta.  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part5.png) 

Kun tapahtuma luodaan ilmestyy se taulukkoon, jokaisen tapahtuman oikeassa laidassa on kaksi nappia: Modify-nappi, joka mahdollistaa tapahtuman muokkauksen ja Remove-nappi, joka mahdollistaa tapahtuman poistamisen.

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part6.png)  

Modify-nappia painamalla pääset näkymään, josta näät muokattavan tapahtuman ja voit laittaa lomakkeeseen muutokset jotka haluat.

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part7.png)

Remove-nappia painamalla pääset näkymään jonka avulla varmistetaan, että halaut varmasti poistaa kuvatun tapahtuman.  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part8.png)  

Lopuksi oikeasta yläkulmasta löytyy Logout-nappi, jonka avulla voit kirjautua ulos ja pääset takaisin kirjautumis-näkymään.  

![Tiedosto](/FinanceTrackerApp/dokumentaatio/kuvat/kayttoohje-part9.png)  


