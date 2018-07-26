import java.sql.*;
import java.util.Date;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.get;
import spark.template.velocity.*;

public class Main {

    public static void main(String[] args) {

        // For MySQL Insert
        String subscribed = "Y";
        Date adddate = new Date();
        Date moddate = new Date();

        staticFiles.location("/public");

            // this is my home page
            get("/", (rq, rs) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "templates/index.vm");
            });

            // when someone subs, take them here
            get("/thanks", (rq, rs) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "templates/thanks.vm");
            });

            // duplicate emails are not allowed
            get("/existing", (rq, rs) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "templates/existing.vm");
            });

            // oops page, for the unexpected
            get("/oops", (rq, rs) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "templates/oops.vm");
            });

            // when someone unsubs, take them here
            get("/bye", (rq, rs) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "templates/bye.vm");
            });

            // get name and email from user input
            post("/input", (request, response) -> {
                String a, b;
                a = request.queryParams("name");
                b = request.queryParams("email");

                if(a != null && b != null){

                    try {
                        // connect to database
                        final String url = "jdbc:mysql://localhost:3306/GoodMorning";
                        final String user = "root";
                        final String password = "Pepper";

                        final Connection conn = DriverManager.getConnection(url, user, password);
                        if (conn != null) {
                        }
                        // sql insert statement
                        final String query = "insert into Users (Name, Email, Subscribed, AddDate, ModDate)"
                                + "values (?, ?, ?, ?, ?)";
                        final PreparedStatement insertUsers = conn.prepareStatement(query);

                        // sql insert values
                        insertUsers.setString(1, a);
                        insertUsers.setString(2, b);
                        insertUsers.setString(3, subscribed);
                        insertUsers.setString(4, adddate.toString());
                        insertUsers.setString(5, moddate.toString());
                        insertUsers.execute(); // execute

                        System.out.println("Successfully added "+a+" / "+b);

                        conn.close(); // close the connection
                        System.out.println("DB Connection has been closed.");
                        response.redirect("/thanks"); // Take the user to the Thank You page

                    } catch (Exception e) {
                        final String url = "jdbc:mysql://localhost:3306/GoodMorning";
                        final String user = "root";
                        final String password = "Pepper";

                        final Connection conn = DriverManager.getConnection(url, user, password);
                        if (conn != null) {
                        }
                        System.err.println("Got an exception!");
                        System.err.println(e.getMessage());
                        response.redirect("/existing"); // Email is a unique key
                        conn.close(); // close the connection
                        System.out.println("DB Connection has been closed.");
                    }
                } else {
                    System.out.println("Name or Email not entered...");
                    response.redirect("/oops"); // How did you even get here?
                }
               return String.join(" / ", a, b);
            });
    }
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}