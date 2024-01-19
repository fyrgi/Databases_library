import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {

        DBConnection.username = "root";
        DBConnection.password = "root";
        DBConnection.database = "Library";

        Connection conn = DBConnection.getConnection();
        if(conn == null){
            System.out.println("Couldn't connect to the database.");
            System.exit(-1);
            return;
        } else {
            Library lib = new Library();
        }
    }
}