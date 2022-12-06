package edu.brown.cs.student.server;

import static spark.Spark.after;

import edu.brown.cs.student.outfit.Closet;
import spark.Spark;

/**
 * Top-level class for project. Contains the main() method which starts Spark and runs the various
 * handlers. We have three endpoints in this server, two of which share a share state of
 * (currentData).
 */
public class Server {
  public static void main(String[] args) {
    Spark.port(3232);
      Closet currentCloset = new Closet();
      //currentCloset.setCloset(null);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Setting up the handler for the closet endpoints.
    Spark.get("closet", new GetClosetHandler(currentCloset));
    Spark.get("edit", new EditClosetHandler(currentCloset));
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
