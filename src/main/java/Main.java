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

        // Be safe out there
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

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

        // favicon
        get("/favicon", (rq, rs) -> {
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

        // when someone wants to unsubs, take them here
        get("/bye", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/bye.vm");
        });

        // when someone unsubs, take them here
        get("/unsubscribe", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "templates/unsubscribe.vm");
        });

        // get name and email from user input
        post("/input", (request, response) -> {
            String name, email;
            name = request.queryParams("name");
            email = request.queryParams("email");

            if (name != null && email != null) {

                try {
                    // connect to database
                    final String url = dbURL;
                    final String user = dbUser;
                    final String password = dbPassword;

                    final Connection conn = DriverManager.getConnection(url, user, password);
                    if (conn != null) {
                    }
                    // sql insert statement
                    final String query = "insert into GoodMorning.Users (Name, Email, SignUpDate)"
                            + "values (?, ?, ?)";
                    final PreparedStatement insertUsers = conn.prepareStatement(query);

                    // sql insert values
                    insertUsers.setString(1, name);
                    insertUsers.setString(2, email);
                    insertUsers.setString(3, adddate.toString());
                    insertUsers.execute(); // execute

                    System.out.println("Successfully added " + name + " / " + email);

                    conn.close(); // close the connection
                    System.out.println("DB Connection has been closed.");
                    response.redirect("/thanks"); // Take the user to the Thank You page

                } catch (Exception e) {
                    final String url = dbURL;
                    final String user = dbUser;
                    final String password = dbPassword;

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
                System.out.println("Name or SubDateSSubsdEmail not entered...");
                response.redirect("/oops"); // How did you even get here?
            }
            return String.join(" / ", name, email);
        });

        // delete a user
        post("/leaving", (request, response) -> {
            String emailBye;
            emailBye = request.queryParams("emailBye");

            if (emailBye != null) {

                try {
                    // connect to database
                    final String url = dbURL;
                    final String user = dbUser;
                    final String password = dbPassword;

                    final Connection connDelete = DriverManager.getConnection(url, user, password);
                    if (connDelete != null) {
                    }
                    // sql delete statement
                    final String delete = "delete from GoodMorning.Users where email = ?";
                    final PreparedStatement deleteUsers = connDelete.prepareStatement(delete);
                    deleteUsers.setString(1, emailBye);
                    deleteUsers.executeUpdate();

                    System.out.println("Successfully deleted " + emailBye);

                    connDelete.close(); // close the connection
                    System.out.println("DB Connection has been closed.");
                    response.redirect("/bye"); // Take the user to the Bye page

                } catch (Exception e) {
                    final String url = dbURL;
                    final String user = dbUser;
                    final String password = dbPassword;

                    final Connection connDelete = DriverManager.getConnection(url, user, password);
                    if (connDelete != null) {
                    }
                    System.err.println("Got an exception!");
                    System.err.println(e.getMessage());
                    response.redirect("/oops"); // Email is a unique key
                    connDelete.close(); // close the connection
                    System.out.println("DB Connection has been closed.");
                }
            } else {
                System.out.println("Email not entered...");
                response.redirect("/oops"); // How did you even get here?
            }
            return String.join("Removing ", emailBye);
        });
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}