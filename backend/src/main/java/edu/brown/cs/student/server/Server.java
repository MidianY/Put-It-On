package edu.brown.cs.student.server;

import static spark.Spark.after;

import edu.brown.cs.student.handlers.*;
import spark.Spark;

/**
 * Top-level class to run our API server. Contains the main() method which starts Spark and runs the
 * various handlers.
 *
 * <p>We have three endpoints: loadcsv, getcsv, and weather. loadcsv and getcsv utilize a shared
 * state, csvDatabase.
 */
public class Server {

  public static void main(String[] args) {
    Spark.port(3230);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.
       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.
       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.
       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    // put state variable shared among classes/methods here i.e closet
    UserData userDB = new UserData();

    // Setting up the handler for the GET endpoints
    Spark.get("storeGetWeatherData", new StoreGetWeatherHandler(userDB));
    Spark.get("getWeatherData", new GetWeatherHandler(userDB));
    Spark.get("getCloset", new GetClosetHandler(userDB));
    Spark.get("getClosetStats", new GetClosetStatsHandler(userDB));
    Spark.get("editCloset", new EditClosetHandler(userDB));
    Spark.get("recc", new RecommenderHandler(userDB));


    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
