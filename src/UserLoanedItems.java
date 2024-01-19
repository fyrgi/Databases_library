import java.sql.Connection;
import java.sql.*;
/*public class UserLoanedItems {
    protected int id = -1;
    protected String title, author, loaned, returned;

    public UserLoanedItems(int idUser, String returned) throws SQLException {
        Connection conn = DBConnection.getConnection();
        if(returned.equals("null")){
        //PreparedStatement ps = conn.prepareStatement("SELECT id, full_name, email, phone, password, date_created FROM user WHERE id = ?");
            //PreparedStatement sql = conn.prepareStatement("SELECT title, author, returned FROM user_loaned_items AS ul JOIN media AS m ON ul.id_media = m.id JOIN media_person AS mp ON m.id = mp.id_media JOIN person AS p ON p.id = mp.id_person JOIN media_type as mt ON m.id_media_type = mt.id WHERE ul.id_user = ? AND ul.returned IS NOT NULL");
            sql.setInt(1, id);
            init(sql);
        } else {
            PreparedStatement sql = conn.prepareStatement("SELECT m.title, m.subtitle, mt.name AS mediatype, CONCAT(p.first_name,\" \", p.last_name) AS media_by, ul.loaned, ul.returned FROM user_loaned_items AS ul JOIN media AS m ON ul.id_media = m.id JOIN media_person AS mp ON m.id = mp.id_media JOIN person AS p ON p.id = mp.id_person JOIN media_type as mt ON m.id_media_type = mt.id WHERE ul.id_user = ? AND ul.returned IS NULL");
            sql.setInt(1, id);
            init(sql);
        }


    }

    public UserLoanedItems(String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, full_name, email, phone, password, date_created FROM user WHERE email = ? AND password = ?");
        ps.setString(1, email);
        ps.setString(2, password);
        init(ps);
    }

    public UserLoanedItems() { }

    private void init(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            this.id = rs.getInt("id");
            this.title = rs.getString("m.title");
            this.subtitle = rs.getString("m.subtitle");
            this.author = rs.getString("media_by");
            this.loaned = rs.getString("ul.loaned");
            this.returned = rs.getString("ul.returned");
        }
    }

    public void print() {
        System.out.printf("Contact: id=%d, title=%s, author=%s, loaned=%s, returned=%s\n", id, title, author, loaned, returned);
    }

}
*/