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
        redirect.post("/input", "/thanks"); // Never let the end user see the post

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

                if(a != null){
                    System.out.println(a);

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

                        System.out.println("Succesfully added");

                        conn.close(); // close the connection
                        System.out.println("DB Connection has been closed.");

                    } catch (Exception e) {
                        System.err.println("Got an exception!");
                        System.err.println(e.getMessage());

                    }
                }else {
                    System.out.println("Waiting...");
                }
               return String.join(" / ", a, b);
            });
        System.out.println("main done");
    }
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}