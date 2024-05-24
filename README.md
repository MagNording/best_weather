The Optimal Liljeholmen Weather!
Lab Assignment 3 – Web Services and Integrations, Java Developer -23

The task is to develop a web application that presents a weather forecast for Liljeholmen. 
The weather should be "optimized" from two open weather forecast services, 
meaning only the best weather should be reported. 
Which weather criteria are considered "best" is optional (but must be documented).

The forecast should be delivered both as a simple web page and as a REST web service. 
The forecast should report the expected weather in approximately 24 hours, i.e., only one specific time. 
It is sufficient if the weather report consists of two values, such as temperature and humidity, 
but it should also report the source of the forecast and the time of the forecast.

Functional Requirements

The server should fetch data using REST from at least two weather forecast services, 
select the "best" weather, and deliver the result both as a simple web page, e.g.,

Optimal weather forecast for Liljeholmen from SMHI 2022-05-02 11:24
Temperature: 12.5 °C
Relative Humidity: 46%

and as a REST web service, e.g.,
{ "origin": "SMHI", "temp": 12.5, "humidity": 46, "timestamp": "2022-05-02T11:24" }
