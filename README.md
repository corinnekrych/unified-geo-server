Unified Geo Server
==================

# Introduction

This is POC on how AeroGear could provide a server component to handle geolocation data for Mobile applications.

Like the UnifiedPush server, for push, or Keycloak, for security, the UGS (Unified Geo Server) manages a particular aspect of your application, in this case the geolocation data.

The main benefits : 
* Avoid implementing "non-core" functionalities in your application, like registering location and doing spatial queries
* Privacy , the UGS can be configured (not yet in the POC) to never expose the geo data but instead use an alias.
* Sharing an UGS instance between different applications.  

# Main Flow

A lot of concepts have been borrowed by the UnifiedPush Server. The idea is to have (geo) applications to which we associate installations. Here we do not have the concept of `variant` since we have a single implementation for the registration and the spatial queries.

* An Admin creates a Application, that will generate an ApiKey and an ApiSecret. These will be used by the client to register to the UGS.
* The client retrieves its geolocation and register to the UGS by providing an alias, a longitude and a latitude.
* A backend application can now make spatial queries to the UGS : retrieve all the installations within a radius of a particular latitude/longitude pair or retrieve all the installation within  a radius of another installation (query by alias).

![](http://s3.postimg.org/5eqjyyy0j/Untitled_Diagram.jpg)


## Integration with Push

The UGS is totally independant from the UPS but here is how both could be integrated :

1. A device is already registered to the UnifiedPush server with, let's say, alias "bob".
2. Bob registers  to the Geo Server using "bob" as alias.
3. A backend Application is notified (by a REST call, AMQ message, whatever) that Bob has registered to UGS and wants to notify the other users within a radius of 1 Km that Bob has checked in. Using the spatial query, it retrieves all the aliases and can now use that as criteria in the UnifiedPush Message.


# Installation

`mvn clean package` and then deploy to Wildfly/EAP.

The console is available at : `http://localhost:8080/unified-geo-server/`


# Exposed services

## Application creation

```
POST http://mygeoserver/rest/application 
{
  "name" : "MyAwesomeApp"
}
```
In the console, after that the app has been created you will have a apiKey and apiSecret : 

![](http://s28.postimg.org/ljjzmfarh/creds.png)
 


## Registration 

Typically a device/client will call the endpoint :

```

The Endpoint is protected using HTTP Basic (credentials apiKey:apiSecret).

POST http://mygeoserver/rest/installations 
{
 "alias" : "sebi"
 "longitude" : 41.233
 "latitude" : 3.2145
} 
```

Later, it will be possible to register several locations, like "home", "work" ... 

## Performing Spatial Queries

Basically there is only one endpoint that retrieves installations within a radius based on : 
* a latitude/longitude pair
* an alias

The radius is expressed in kilometer (for now).

```
The Endpoint is protected using HTTP Basic (credentials apiKey:apiSecret).

on alias : 

GET http://mygeoserver/rest/installations/geosearch?alias=stalker&radius=10

on latitude/longitude 

GET http://mygeoserver/rest/installations/geosearch?latitude=1.23&longitude=42.35&radius=10

Response : a JSON array of installations.

```

# Actual Server Implementation 

This POC is an JEE app running under Wildfly/EAP. It's using RestEasy for the rest services. For the spatial queries, [Hibernate Search](http://docs.jboss.org/hibernate/search/4.2/reference/en-US/html/spatial.html) is used. Under the hood Hibernate Search uses Lucene and the `installations` are indexed on latitude and longitude. 

But it would be nice to at least 2 other implementations : 
* NojeJS + MongoDB , since mongoDB has also really good support for geo data
* Vertx Mode + MongoDB

