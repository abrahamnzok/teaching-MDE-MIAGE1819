import static spark.Spark.*;

public class DevServer {

	public static void main(String[] args) {
		get("/", (req, res) -> {
            return "hello from sparkjava.com";
        });
	}

}
