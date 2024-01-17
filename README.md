# Protocol
## Technical steps
This program consists of a HTTP/REST-based server and an app that runs on it.
### Server

   * #### The Server class:  
     The server class represents a simple server that listens for incoming socket connections on port 10001 and handles each connection with a separate thread and a RequestHandler.

   * #### The RequestHandler:  
     The RequestHandler handles incoming HTTP requests from clients and generates appropriate HTTP responses.
     The constructor accepts a Socket representing the client connection and a ServerApplication instance. The run-method serves as the entry point for the thread, calling the handle method which processes the HTTP request by reading and converting it into a Request object. The handle-method then uses a ServerApplication instance to obtain a corresponding Response object, sends the response back to the client, and closes the streams and socket.

   * #### The util Package:  
     The util Package contains the HttpMapper. The HttpMapper is a utility class designed to convert HTTP requests and responses between string format and corresponding Java objects. It contains methods for parsing an HTTP request string into a Request object and converting a Response object into an HTTP response string.

   * #### The http Package:
     The http Package contains the Response and the Request classes, as well as three enums defining different types of HttpStatus, HttpMethods and HttpContentTypes, that help creating the needed Responses for each request.

### Mtcg App

   * #### Controllers:  
     The controllers tell the app which routes and request methods they support. A request that is supported by them gets forwarded to the specific method designed for it. Those methods parse the requests' body if needed and send the retrieved information to the respective service.
     In return, they get the data they need to create a Response that then gets returned to the app, then the server and lastly the client.

   * #### Services:
     The services receive the information they need from the controllers and perform the required logic for each request.
     They connect to the repositories and control what gets saved into the database.
     If the request is not valid (e.g. the user is not logged in but wants to update his profile), the services throw respective exceptions, that are caught by the app and sent to the client.

   * #### Repositories:  
     The repositories enable the communication with the connected database.
     They receive data from the services and get, update or delete data from the database.

   * #### Entities:  
     The entities represent the tables that exist in the database as objects.

   * #### The JsonParser:  
     The JsonParser is a class that is accessible to all controllers and enables them to parse needed objects for the services from the RequestBody and vice versa.

   * #### Game:  
     This package contains the BattleLogic Class that is needed for the battle. It contains the logic of the battles.

   * #### Exceptions:  
     The HttpStatusException is a custom exception extending the RuntimeException, that allows Repositories and Services to throw custom errors, which then get caught in the app.

   * #### Dto:  
     The Dto package contains the BattleLog and the BattleRoundResult classes, that both help create the Battle's BattleLog. The BattleRoundResult represents each round of the battle as an object and the BattleLog class puts together the BattleLogString that gets returned to the client once the battle is finished.

   * #### Data:  
     The Database Class provides the connection to the postgres database used for persisting the data.

   * #### Injector:
     The Injector creates all Objects needed for the app handling dependency injections.

## Lessons learned  
This project taught me a lot about the interaction between controllers, services and repositories.
At first, it was difficult for me to separate those, but as time continued, I managed to separate the creation of the response, the logic and the interaction with the database as good as I can.  
Another very important lesson was the way unit tests worked in Java, as I have never written some in Java myself before.
It took some time for me to manage the mocking of different data, but after a few written tests, I got the hang of it.

## Unit Tests  
Unit Tests test single methods for their correct behaviour. In order to do so, other classes are mocked using the Mockito library.
For certain tests, it was necessary to user a spy of a class, to enable mocking single methods in the same class of the method that is tested.  
### Controllers  
  The supports and handle method of each controller are tested with both positive and negative tests. To do so, the tests mock different routes and request methods to check whether the controller can support and handle the correct ones and rejects false ones.
### Services  
  Both the UserService and the SessionService are tested because they are essential for all other routes to work properly.
  Tests include saving users, updating their data and the login.
### Repositories  
  The UserRepostory is tested in order to guarantee the correct behaviour of the database.

## Unique feature  
Users are able to retrieve their own history of battles using the /history route, the request method "GET" and their authorization token as header.

## Time spent  
I spent about 120 hours on this project.

## Link to repo  
https://github.com/elektrolena/MonsterTradingCardsGame
