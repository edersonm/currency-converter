# Currency Converter

How to start the Currency Converter application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/currency-converter-1.0-SNAPSHOT.jar server config.yml`

Usage
---

1. To get the latest currency `http://localhost:8080/currency/get_current`

1. To get the currency history `http://localhost:8080/currency/get_by_interval?beginDate=2017-09-02T00:00:00.000&endDate=2017-09-06T23:59:00.000&queryByRequestDate=false` or `http://localhost:8080/currency/get_by_interval?beginDate=2017-09-02T00:00:00.000&endDate=2017-09-03T23:59:00.000&queryByRequestDate=true`

1. To configure the assyncronous update time: `http://localhost:8080/scheduler/change_update_time?intervalInMinutes=10`

1. To configure the maximum cache time: `http://localhost:8080/scheduler/change_db_cache_time?intervalInSeconds=40`


Notes
---
1. On "To get the currency history" the or stands for the difference between queryByRequestDate, if true, the search will consider the date the request was made. If false, it will consider the currency date, for example: If a request is made on Sunday, the "currency date" for that request is Friday, which is the last valid date from the provider.

1. On "To configure the assyncronous update time" we configure the maximum interval, in minutes, for the assyncronous request to be made and stored in database. The default value is 1.

1. On "To configure the maximum cache time", we have implemented a cache in memory to avoid a high number of request to the provider in a short period of time. The cache will verify if in the inverval now - intervalInSeconds there is any request with for the same date. If yes, it will response will the data stored locally. To use no cache, just set intervalInSeconds to <= 0, the default value is 3.

ATENTION:
---
Make sure to have Internet access to `http://api.fixer.io/`