# MacroFinData

MacroFinData API is free and will be gradually expanded to offer different types of macroeconomic data in JSON format. 

## Version 1.0

Version 1.0 provides data about the current and historical foreign exchange reference rates of the European Central Bank (ECB) in JSON format. The reference rates are usually updated around 16:00 CET on every working day, except on TARGET closing days. 

Users can receive data about the reference rate recalculated into any of the currencies on the list of the ECB.

A currency exchange calculator is also available, based on the current or historical exchange rates, as specified by the client.

### API Response

The API returns foreign exchange data as JSON objects per each currency, wrapped in a single collection object:


```json
{
    "base": "EUR",
    "date": "2020-08-28",
    "list": [
        {
            "code": "BGN",
            "value": "1.9558",
            "currencyName": "Bulgarian lev",
            "source": "European Central Bank"
        }
    ]
}
```

The collection object includes:
 - "base" - the base currency against which exchange rates are relative to, by default the base is EUR 1.0
 - "date" - the date on which the reference applies
 - "list" - a collection of returned currency objects

 The currency objects include:
  - "code" - the ISO code of the currency
  - "value" - the value of the currency against the specified base
  - "source" - the source of the reference rate

  ### Endpoints
  
  Base URL:

```http
GET  macrofin-data.herokuapp.com/api/currency
```   

This API comes with endpoints delivering different functionalities. 
- **Newest rates**<br/>
Responds with the reference rates in force at the time of the request
- **Available currencies**</br>
Returns the available currencies against which currency rates are available
- **History**</br>
Returns the latest reference rates available at a specific date after January 9, 1999
- **Time series**</br>
Responds with paginated reference rate time series under criteria specified by the client
- **Converter**</br>
Returns converted values under criteria specified by the client

#### Newest rates

The **newest** endpoint without  will return real-time exchange rate data for all currencies for which the ECB offers a reference rate:

```http
GET  macrofin-data.herokuapp.com/api/currency/newest
```   

* By default the API returns the value of currencies against 1 .0 EUR

There are optional parametres
```http
GET  macrofin-data.herokuapp.com/api/currency/newest

?codes=bgn,usd,dzd

&base=bgn
```    
* The ***codes*** parametre returns a list of currencies according to the client request. By default the API returns the lits of all available reference rates. The list of available currecies can be checked at the **available-currencies** endpoint.

* The ***base*** parametre calculates the value of the exchange rates against the specified currency, which should be on the list of available currencies.

The currencies are specified with their ISO code and with a ***,*** separating them (without space).

#### Available currencies

The **available-currencies** endpoint returns the list of currently available currencies:

```http
GET  macrofin-data.herokuapp.com/api/currency/available-currencies
```    

The API responds with the ISO code of the currency and its name. The endpoint above would return:

```json
"currencies": {
        "ARS": "Argentine peso",
        "AUD": "Australian dollar",
        [...]
        "USD": "US dollar",
        "ZAR": "South African rand"
    }
}
```  
However, as the coverage of some currencies was suspended after 1999 (as the currencies have joined the Eurozone or for other reasons) you have the option to get all the currencies for which there is reference rate data on the ECB server with:

```http
GET  macrofin-data.herokuapp.com/api/currency/available-currencies

?ever=true
``` 

#### History

The history endpoint returns the reference rate data for a specific day after after January 9, 1999.

* The ***date*** parametre is obligatory, it specifies the date for which the client needs to get results in yyyy=mm=dd format

The obligatory endpoint is:

```http
macrofin-data.herokuapp.com/api/currency/history

?date=2011-05-06 
``` 

* The ***base*** parametre is optional. It will return the values of rates calculated against 1 unit of a specified currency. The default value is EUR

```http
GET macrofin-data.herokuapp.com/api/currency/history

?date=2011-05-06 

&base=usd
``` 

#### Time series

The **time-series** endpoint responds with a *paginated* list of currencies for a period of time specified by the client. 

The ***start*** and ***end*** parametres are obligatory, as they specify the start and end date of the time series.


The client may also specify:

* *(Optional)* The number of currencies per page. By default the number is 50 - ***limit*** parametre
* *(Optional)* The page of the returned selection. By default the page is 1. - ***page*** parametre
* *(Optional)* The base currency against which values are calculated. It will return the values of rates calculated against 1 unit of a specified currency. The default value is EUR - ***base*** parametre
* *(Optional)* A list of currencies according to the client request. By default the API returns the lits of all available reference rates. The list of available currecies can be checked at the **available-currencies** endpoint - **codes** parametre

An HTTP request with all available parametres looks like this:

```http
GET macrofin-data.herokuapp.com/api/currency/timeseries

?start=2011-06-05 (obligatory)

&end=2012-04-29 (obligatory)

&limit=25 (optional)

&page=3 (optional)

&base=usd (optional)

&codes=dzd,bgn,aud (optional)
``` 

#### Converter

The **converter** endpoint allows the conversion of the value of currencies into a currency specified by the client.

* The **from** and **to** parametres are obligatory. They indicate the currencies the client wishes to convert.
* (Optional) The client may specify a date for which a conversion is to be made in the yyyy-mm-dd format. The default value is today - **date**
* The **amount** value indicates the amount we wish to convert. Decimal values are accepted. The default value is 1.0 - **amount**

An HTTP request with all available parametres looks like this:

```http
GET macrofin-data.herokuapp.com/api/currency/converter

?from=eur&to=dzd (obligatory)

&date=2019-04-03 (optional)

&amount=15.36 (optional)

&base=usd (optional)
``` 

The API response would be:

```json
[
    {
        "query": {
            "amount": "15.36",
            "from": "EUR",
            "to": "DZD"
        },
        "rate": "134.2639",
        "date": "2019-04-03",
        "result": "2062.2935",
        "source": "European Central Bank"
    }
]
```

* ***query*** list includes details about the request
* ***rate*** is the reference rate according to which the calculation was made
* ***result*** is the result of the conversion
* ***source*** is the source of the reference rate

The **converter/timeseries** endpoint returns a list of converted currency values for a specific period of time. 

* The ***from*** and ***to*** parametres are *obligatory*. They indicate the currencies the client wishes to convert.
* The **start_date** and **end_date** parametres are *obligatory*, as they specify the start and end date of the time series.
* *(Optional)* The number of currencies per page. By default the number is 50 - ***limit*** parametre
* *(Optional)* The page of the returned selection. By default the page is 1. - ***page*** parametre
* The **amount** value indicates the amount we wish to convert. Decimal values are accepted. The default value is 1.0 - **amount**

An HTTP request with all available parametres looks like this:

```http
GET macrofin-data.herokuapp.com/api/currency/converter/timeseries

?from=eur&to=dzd (obligatory)

&start_date=2019-04-03&end_date=2020-02-25 (optional)

&limit=25 (optional)

&page=3 (optional)

&amount=15.36 (optional)
``` 

### Errors

Whenever an API call fails, an error code, timestamp and message are returned in JSON format:

```json
{
    "path": "/api/currency",
    "error code": 404,
    "message": "The requested API endpoint does not exist!",
    "status": "NOT_FOUND",
    "timestamp": "2020-08-29T14:12:47.699"
}
```
Possible Errors:

| Error  | Description |
|-----|-----|
| 400  |  Incorrect parametre |
| 404 | The requested resource does not exist  |
| 422 | Invalid base currency parametre  |
| 500 | Internal server error  |
| 502 | No connection with ECB server   |

## Author

My name is Radomir Ralev, I am based in Sofia, Bulgaria. This is my first open source project. Programming has become my hobby in the past year or so as I'm deeply convinced that computers should be used to make our lives easier and their development is a huge step forward for the entire humanity. I hope the project will be useful for the community!

If you wish to contact me, you can drop me an [email](rado_ralev@yahoo.com), or check my [LinkedIn](https://www.linkedin.com/in/radomir-ralev-02371b13/) profile.

Happy programming!

## License

[MIT](https://choosealicense.com/licenses/mit/)

