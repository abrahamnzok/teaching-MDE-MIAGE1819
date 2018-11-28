package main;

import static spark.Spark.*;

import java.util.Random;

public class Main {

  public static void main(String[] args) {
    port(8080);

    DevServer server = new DevServer();

    path("/compile/", () -> {
      get("autogenerate/", (req, res) -> {
        return server.generate(req, res);
      });
      get("gif/", (req, res) -> {
        return server.getGif(req, res);
      });
    });

    path("/variant/", () -> {
      get("/variant/duration", (req, res) -> {
        return server.getVariantDuration(req, res);
      });
      get("/variant/possibilities/data", (req, res) -> {
        return server.getPossibleVariansAndSize(req, res);
      });
    });
  }

}
