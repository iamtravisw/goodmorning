import static spark.Spark.*;
import java.sql.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {


        get("/input", (request, response) -> {
            return "Hello: " + request.params(":email");
        });

        get("/goodmorning/public/index.html", (request, response) -> {
            return "Hello: " + request.params(":name");
        });

        post("/input", (request, response) -> {
            String a, b;
            a = request.queryParams("name");
            b = request.queryParams("email");
            return String.join(" AND ", a, b);
        });

    }

    public static void sendData(String[] args) {

        String name = "";
        String email = "";
        String subscribed = "Y";
        Date adddate = new Date();
        Date moddate = new Date();

        Connection conn = null;
        try {
            // connect to database
            String url = "jdbc:mysql://localhost:3306/GoodMorning";
            String user = "root";
            String password = "Pepper";

            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
            }

            System.out.println("Inserting...");

            // insert statement
            String query = "insert into CharacterBasics (Name, Email, Subscribed, AddDate, ModDate)"
                    + "values (?, ?, ?, ?, ?)";
            PreparedStatement insertUsers = conn.prepareStatement(query);
            insertUsers.setString(1, name);
            insertUsers.setString(2, email);
            insertUsers.setString(3, subscribed);
            insertUsers.setString(4, adddate.toString());
            insertUsers.setString(5, moddate.toString());
            insertUsers.execute(); // execute

            System.out.println("Succesfully added");

            conn.close(); // close the connection
            }
            catch (Exception e)
            {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
}