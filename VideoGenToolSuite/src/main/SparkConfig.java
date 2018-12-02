package main;

import java.util.HashMap;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.*;

public final class SparkConfig {
private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();
    
    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "false");
    }

    public final static void apply() {
    	  options("/*",
    	    (request, response) -> {

    	      String accessControlRequestHeaders = request
    	        .headers("Access-Control-Request-Headers");
    	      if (accessControlRequestHeaders != null) {
    	        response.header("Access-Control-Allow-Headers",
    	          accessControlRequestHeaders);
    	      }

    	      String accessControlRequestMethod = request
    	        .headers("Access-Control-Request-Method");
    	      if (accessControlRequestMethod != null) {
    	        response.header("Access-Control-Allow-Methods",
    	          accessControlRequestMethod);
    	      }

    	      return "OK";
    	    });

    	  before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    	}

}
