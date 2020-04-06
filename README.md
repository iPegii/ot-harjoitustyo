# Finance Tracker App

Sovelluksen tarkoituksena on mahdollistaa kulujen seuranta.


## Dokumentaatio
### [Vaatimusmäärittely](https://github.com/iPegii/ot-harjoitustyo/blob/master/FinanceTrackerApp/dokumentaatio/vaatimusmaarittely.md)  
### [Tuntikirjanpito](https://github.com/iPegii/ot-harjoitustyo/blob/master/FinanceTrackerApp/dokumentaatio/tuntikirjanpito.md)

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


