import com.sendgrid.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class DailyEmail {

        public static void main(String[] args) throws IOException {

            // Be safe out there
            String dbURL = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            try {
                // connect to database
                String url = dbURL;
                String user = dbUser;
                String password = dbPassword;
                Connection conn = null;
                Date lastuse = new Date();

                conn = DriverManager.getConnection(url, user, password);
                if (conn != null) {

                    // SELECT for Images
                    Statement stmtImages  = conn.createStatement();
                    ResultSet rsImages = stmtImages .executeQuery("SELECT * FROM GoodMorning.Images ORDER BY (CASE WHEN lastuse IS NULL THEN imageid WHEN lastuse IS NOT NULL THEN LastUse END) ASC LIMIT 1;");
                    while (rsImages .next()) {
                        //Retrieve by column name
                        String randomURL = rsImages .getString("URL");
                        String randomImageID = rsImages .getString("ImageID");

                        // update the data
                        PreparedStatement psImages = conn.prepareStatement(
                                "UPDATE GoodMorning.Images SET LastUse = ? WHERE URL = ?");

                        // set the preparedstatement parameters
                        psImages.setString(1, lastuse.toString());
                        psImages.setString(2, randomURL);
                        psImages.executeUpdate();
                        System.out.println("Updated record: " + randomImageID + ". Which can be found at: https://storage.googleapis.com/goodmorning-photos/" + randomURL);
                        psImages.close();

                        // SELECT for Subjects
                        Statement stmtSubjects = conn.createStatement();
                        ResultSet rsSubjects = stmtSubjects.executeQuery("SELECT * FROM GoodMorning.Subjects ORDER BY (CASE WHEN lastuse IS NULL THEN subjectid WHEN lastuse IS NOT NULL THEN LastUse END) ASC LIMIT 1;");
                        while (rsSubjects.next()) {
                            //Retrieve by column name
                            String randomSubject = rsSubjects.getString("SubjectLine");
                            String randomSubjectID = rsSubjects.getString("SubjectID");

                            // update the data
                            PreparedStatement psSubjects = conn.prepareStatement(
                                    "UPDATE GoodMorning.Subjects SET LastUse = ? WHERE SubjectLine = ?");

                            // set the preparedstatement parameters
                            psSubjects.setString(1, lastuse.toString());
                            psSubjects.setString(2, randomSubject);
                            psSubjects.executeUpdate();
                            System.out.println("Updated record: " + randomSubjectID + ". Subject: " + randomSubject);
                            psSubjects.close();


                            // SELECT for Users
                            Statement stmtUsers = conn.createStatement();
                            ResultSet rsUsers = stmtUsers.executeQuery("SELECT * FROM GoodMorning.Users;");
                            while (rsUsers.next()) {
                                //Retrieve by column name
                                String gmUser = rsUsers.getString("Email");
                                String gmUserID = rsUsers.getString("UserID");

                                // update the data
                                PreparedStatement psUsers = conn.prepareStatement(
                                        "UPDATE GoodMorning.Users SET LastEmailed = ? WHERE Email = ?");

                                // set the preparedstatement parameters
                                psUsers.setString(1, lastuse.toString());
                                psUsers.setString(2, gmUser);
                                psUsers.executeUpdate();
                                System.out.println("Updated record: " + gmUserID + ". Subject: " + gmUser);
                                psUsers.close();

                                // Moving on to SendGrid now
                                Email from = new Email("GoodMorning@GoodMorning.email");
                                String subject = randomSubject;
                                Email to = new Email(gmUser);

                                String htmlStart = "<html><body><p>Here is your daily motivation.</p><p><img alt=\"\" title=\"\" src=\"";
                                String htmlEnd = "\" style=\"border:none;border-radius:0px;display:block;font-size:13px;outline:none;text-decoration:none;\" ></p><p><a href=\"mailto:iamtravisw@gmail.com\">Report a problem</a>.<br>Tired of GoodMorning? <a href=\"http://goodmorning.email/unsubscribe\">Unsubscribe here</a>.</p></body></html>";

                                String html_content = String.format("%shttps://storage.googleapis.com/goodmorning-photos/%s%s", htmlStart, randomURL, htmlEnd);
                                Content content = new Content("text/html", html_content);
                                Mail mail = new Mail(from, subject, to, content);

                                String sendGridAPIKey = System.getenv("SENDGRID_API_KEY");
                                SendGrid sg = new SendGrid(sendGridAPIKey);

                                Request request = new Request();
                                try {
                                    request.setMethod(Method.POST);
                                    request.setEndpoint("mail/send");
                                    request.setBody(mail.build());
                                    Response response = sg.api(request);
                                    System.out.println(response.getStatusCode());
                                    System.out.println(response.getBody());
                                    System.out.println(response.getHeaders());
                                } catch (IOException ex) {
                                    throw ex;
                                }
                            }
                        }
                    }
                }
                } catch(Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
        }
    }