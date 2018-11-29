package main;

import static spark.Spark.*;

public class Main {

  public static void main(String[] args) {
    port(8080);

    DevServer server = new DevServer();

    path("/allmedias/", () -> {
      get("", (req, res) -> {
        return server.getMedias(req, res);
      });
    });
    path("/compile/", () -> {
      get("autogenerate/", (req, res) -> {
        return server.generate(req, res);
      });
      get("gif/", (req, res) -> {
        return server.getGif(req, res);
      });
    });

    path("/variant/", () -> {
      get("duration/", (req, res) -> {
        return server.getVariantDuration(req, res);
      });
      get("possibilities/data/", (req, res) -> {
        return server.getPossibleVariansAndSize(req, res);
      });
    });
  }

}
