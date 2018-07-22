import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/index", (req, res) -> "Test");
    }
}