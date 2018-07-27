import java.sql.*;
import java.util.Date;

public class Rotation {

    public static void main(String[] args) {

            try {
                // connect to database
                String url = "jdbc:mysql://localhost:3306/GoodMorning";
                String user = "root";
                String password = "Pepper";
                Connection conn = null;
                Date lastuse = new Date();

                conn = DriverManager.getConnection(url, user, password);
                if (conn != null) {

                    // select the data
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM GoodMorning.Images ORDER BY (CASE WHEN lastuse IS NULL THEN imageid WHEN lastuse IS NOT NULL THEN LastUse END) ASC LIMIT 1;");
                    while(rs.next()) {
                        //Retrieve by column name
                        String randomURL = rs.getString("URL");
                        String randomImageID = rs.getString("ImageID");

                        // update the data
                        PreparedStatement ps = conn.prepareStatement(
                                "UPDATE GoodMorning.Images SET LastUse = ? WHERE URL = ?");

                        // set the preparedstatement parameters
                        ps.setString(1,lastuse.toString());
                        ps.setString(2,randomURL);
                        ps.executeUpdate();
                        System.out.println("Updated record: "+randomImageID+". Which can be found at: "+randomURL);
                        ps.close();
                    }
                }

            } catch(Exception e){
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
        }
    }