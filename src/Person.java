import java.sql.Connection;
import java.sql.*;
public class Person {
    protected int id = -1; // -1 means a "new" record
    protected String firstName;
    protected String lastName;
    protected String nickname;



    public Person(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, first_name, email, phone, password, date_created FROM user WHERE id = ?");
        ps.setInt(1, id);
        init(ps);
    }

    public Person() { }

    private void init(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            this.id = rs.getInt("id");
            this.firstName = rs.getString("first_name");
            this.lastName = rs.getString("last_name");
            this.nickname = rs.getString("nickname");
        }
    }

    /*
            Getters and Setters
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String nickname() {
        return this.nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }

    // --

    public void print() {
        System.out.printf("Person: id=%d, first name=%s, last name=%s, nickname=%s\n", id, firstName, lastName, nickname);
    }

    public int save() throws SQLException {

        String sql = "";

        if(id > 0) {
            sql = "UPDATE person SET first_name = ?, last_name = ?, nickname = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO person SET first_name = ?, last_name = ?, nickname = ?";
        }

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, nickname);
        if(id > 0) {
            ps.setInt(4, id);
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

    public int delete() throws SQLException {

        if(id < 0) {
            throw new SQLException("No person loaded!");
        }

        PreparedStatement ps = DBConnection
                .getConnection()
                .prepareStatement(
                        "DELETE FROM Person WHERE id = ?"
                );
        ps.setInt(1, this.id);
        int numberOfChangedRows = ps.executeUpdate();
        if(numberOfChangedRows > 0) {
            this.id = -1;
        }
        return numberOfChangedRows;
    }
}
