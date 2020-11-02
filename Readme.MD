# CurrencyFair Coding Assesment
### Technologies Used
* Language: Java
* Framework: SpringBoot
* Database: Embedded Database - Apache Derby

As the application uses an embedded database, every time it is being restarted the data inserted will be erased.

### Usage
* To run the jar, either clone the repository and run "maven clean install" to run the tests and build the jar or, clone the repository and run the jar from the target directory 
* Note: it is required to have jdk installed on your computer/server
```
java -jar target/currencyfair-1.0-SNAPSHOT.jar
```
In order to populate Database with trade messages send a POST request to the http://localhost:8080/api endpoint with a JSON body similar to the following
```
{
    "userId": "134256",
    "currencyFrom": "EUR",
    "currencyTo": "GBP",
    "amountSell": 1000,
    "amountBuy": 747.10,
    "rate": 0.7471,
    "timePlaced": "24-JAN-18 10:27:44",
    "originatingCountry": "FR"
}
```

In order to view the data, access the following endpoint from your browser: http://localhost:8080/api 