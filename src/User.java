import java.sql.Connection;
import java.sql.*;
public class User {
    protected int id = -1;
    protected String name;
    protected String email;
    protected String password;
    protected String date_created;

    public User(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, full_name, email, password, date_created FROM user WHERE id = ?");
        ps.setInt(1, id);
        init(ps);
    }

    public User(String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, full_name, email, password, date_created FROM user WHERE email = ? AND password = ?");
        ps.setString(1, email);
        ps.setString(2, password);
        init(ps);
    }

    public User() { }

    private void init(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            this.id = rs.getInt("id");
            this.name = rs.getString("full_name");
            this.email = rs.getString("email");
            this.password = rs.getString("password");
            this.date_created = rs.getString("date_created");
        }
    }

    /*
            Getters and Setters
     */
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void print() {
        System.out.printf("Contact: id=%d, name=%s, email=%s, password=%s\n", id, name, email, password);
    }

    public int save() throws SQLException {

        String sql = "";

        if(id > 0) {
            sql = "UPDATE user SET full_name = ?, email = ?, password = ?, date_update = CURDATE() WHERE id = ?";
        } else {
            sql = "INSERT INTO user SET full_name = ?, email = ?, password = ?, date_created = CURDATE()";
        }

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        if(id > 0) {
            ps.setInt(5, id);
        }

        int numberOfChangedRows = ps.executeUpdate();

        if (id < 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
        }

        return numberOfChangedRows;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
