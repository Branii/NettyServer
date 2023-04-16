import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conf {

    public static Connection getConnection(){

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/lottery";
        String user = "enzerhub";
        String pass = "enzerhub";

        try {
            // Connect
            Connection con = DriverManager.getConnection(url, user, pass);
            if(con != null){
               // System.out.println("Connection to Mysql successful");
                return con;
            }else{
                System.out.println("Connection to Mysql failed");
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public static void closeConnection(){
        try {
            getConnection().close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}

