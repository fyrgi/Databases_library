import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    public static String url = "localhost";
    static int port = 3306;
    public static String username = "";
    public static String password = "";
    public static String database = "";

    /*
        Private variables
    */
    private static DBConnection db;

    private MysqlDataSource dataSource;
    // logikata se izgotvq chrez 2 stypki
    // pyrvo iztochnikyt na dannite
    // posle i konfiguracionnite danni
    // nakraq se syzdava i
    private DBConnection(){}

    private void initDataSource(){
        try {
            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            // trqbva da se syzdade specialen adres kato inicializirame baza danni
            // ako e oracle ili druga mojeshe da se importira drug file a ne tozi connector za mysql
            dataSource.setURL("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private Connection createConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e){
            SQLExceptionPrint(e);
            return null;
        }
    }

    // za da imame samo edna shte.
    // kazva se singleton
    public static Connection getConnection(){
        if(db == null){
            db = new DBConnection();
            db.initDataSource();
        }
        return db.createConnection();
    }

    // iskame da pokajem vsichki syobshteniq
    public static void SQLExceptionPrint(SQLException sqlException, boolean printStackTrace){
        while(sqlException != null){
            System.out.println("\n--SQLException Caught---\n");
            System.out.println("SQL state: " + sqlException.getSQLState());
            System.out.println("Error code: " + sqlException.getErrorCode());
            System.out.println("Message: " + sqlException.getMessage());
            if(printStackTrace) sqlException.printStackTrace();
            sqlException = sqlException.getNextException();
        }
    }

    public static void SQLExceptionPrint(SQLException sqlException){
        SQLExceptionPrint(sqlException, false);
    }
}
