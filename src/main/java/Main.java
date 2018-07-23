import static spark.Spark.*;
import java.sql.*;

import java.sql.Timestamp;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        get("/input/", (request, response) -> {
            return "Hello: " + request.params(":name");
        });
    }

    public static void sendData(String[] args) {



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
            String query = "insert into CharacterBasics (Email, Subscribed, AddDate, ModDate)"
                    + "values (?, ?, ?, ?)";
            PreparedStatement insertUsers = conn.prepareStatement(query);
            insertUsers.setString(1, email);
            insertUsers.setString(2, subscribed);
            insertUsers.setString(3, adddate.toString());
            insertUsers.setString(4, moddate.toString());
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