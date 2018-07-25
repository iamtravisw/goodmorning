
import java.sql.*;
import java.util.Date;
import static spark.Spark.*;


import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Spark;

import static spark.Spark.get;
import spark.template.velocity.*;

// Error:(14, 31) java: package spark.template.velocity does not exist


public class Main {

    public static void main(String[] args) {

        staticFiles.location("/public");

        get("/", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/index.vm");
        });

        // route to user subscribing
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
            String query = "insert into Users (Name, Email, Subscribed, AddDate, ModDate)"
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

    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}