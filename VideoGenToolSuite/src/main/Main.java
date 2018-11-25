package main;

import static spark.Spark.*;

import java.util.Random;

public class Main {

  public static void main(String[] args) {
	  port(8080);
	  
	  get("/variant/duration", (req, res) -> {
		  return new DevServer().getVariantDuration(req, res);
	  });
	  get("/variant/possibilities/data", (req, res) -> {
		  return new DevServer().getPossibleVariansAndSize(req, res);
	  });
  }

}
