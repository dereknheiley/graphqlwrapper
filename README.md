### Task

Design and implement a production-ready REST API that accepts a list of country codes and returns a list of country codes that are in the same continent as the country code input.

Use the endpoint at https://countries.trevorblades.com/graphql to get the up-to-date data required for you to implement your API.

#### [Rate Limit]
In order to not overwhelm graphql server and reduce network traffice, cache the entire continents output.
(Bonus) In order to not overwhelm our rest api server, use a rate limit of 5 requests per sec for unauthenticated users and 20 requests per second for authenticated users.

#### Example 
```
http://localhost:8080/continents?countries=CA,US
```

the expected output is:
```
{
  continent: [{
    countries: ["CA", "US"],
    name: "North America",
    otherCountries: ["AG", "AI", "AW", "BB", ...] // should not have CA or US here
  }]
}
```

### Assumptions
1. Network traffic to graphQL endpoint is not 100% reliable, and our wrapper API should continue to work even if graphQL endpoint is sometimes unreachble.
2. Performance (cpu & memory) is not a limiting factor - the entire dataset can easily fit into memory several times over.
3. This app may be run in parrallel and scalled up to meet higher levels of demands behind a load balancer (outside of scope).

### How To Run
Dockerfile that builds this app into an image that is ready to be ran.
```
gradle clean bootJar

docker build -t graphql-sprint-boot-wrapper-rest-api .
docker run -it -p 8080:8080 graphql-sprint-boot-wrapper-rest-api

docker ps -a
docker stop graphql-sprint-boot-wrapper-rest-api
```