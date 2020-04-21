# Finance Tracker App

Sovelluksen tarkoituksena on mahdollistaa kulujen seuranta.


## Dokumentaatio
### [Vaatimusmäärittely](https://github.com/iPegii/ot-harjoitustyo/blob/master/FinanceTrackerApp/dokumentaatio/vaatimusmaarittely.md)  
### [Tuntikirjanpito](https://github.com/iPegii/ot-harjoitustyo/blob/master/FinanceTrackerApp/dokumentaatio/tuntikirjanpito.md)

### [Arkkitehtuuri](https://github.com/iPegii/ot-harjoitustyo/blob/master/FinanceTrackerApp/dokumentaatio/arkkitehtuuri.md)


## Ennen ohjelman ajamista

Lisää ohjelman juurikansioon (kansioon, jossa sijaitsee esimerkiksi tiedosto "config.properties" ja kansio "dokumentaatio"), tiedosto nimeltä "keys.properties" tämän kansion sisälle kirjoitetaan vain ensimmäiselle riville seuraava: `mongodb.uri=mongodb+srv://<Username>:<Password>@<ClusterName>/test?retryWrites=true&w=majority`

Eli vaihda `<Username>` ja `<Password>` ja `<ClusterName>` vastaamaan omaa MongoDb-tietokantaasi. 

## Komentorivitoiminnot

### Ohjelman ajaminen

Sovelluksen ajettavan jar-tiedoston luominen seuraavalla komennolla

`mvn package`

Sovelluksen ajaminen onnistuu komennolla
`mvn compile exec:java -Dexec.mainClass=financetrackerapp.Main`

### Testaaminen
Ajaa testit ja ilmoittaa mahdollisista virheistä.

`mvn test`

Testikattavuusraportin luominen onnistuu komennolla

`mvn jacoco:report`

Checkstyle raporting luonti

`mvn jxr:jxr checkstyle:checkstyle`


