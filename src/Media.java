import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.*;
public class Media {
    protected int id = -1; // -1 means a "new" record
    protected String title, mediaType, author, avb = "No", rsv = "No";
    protected boolean available, reserved;


    public Media(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, title, author, media_type, available, reserved FROM media WHERE id = ?");
        ps.setInt(1, id);
        init(ps);
    }

    public Media(String search) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, title, author, media_type, available, reseerved FROM media WHERE title LIKE \"%\"?\"%\" OR author LIKE \"%\"?\"%\" OR media_type LIKE \"%\"?\"%\"");
        ps.setString(1, search);
        init(ps);
    }

    public Media() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, title, author, media_type, available, reserved FROM media");
        init(ps);
    }

    private void init(PreparedStatement ps) throws SQLException {
        String[] columnNames = {"ID","Title", "By", "Media type", "Available", "Reserved"};
        DefaultTableModel tm = new DefaultTableModel(columnNames, 0);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            this.id = rs.getInt("id");
            this.title = rs.getString("title");
            this.author = rs.getString("author");
            this.mediaType = rs.getString("media_type");
            this.available = rs.getBoolean("available");
            this.reserved = rs.getBoolean("reserved");
            if(available){
                avb = "Yes";
            }
            if(reserved){
                rsv = "Yes";
            }
            String[] data = {String.valueOf(id), title, author, mediaType, avb, rsv};
            tm.addRow(data);
        }
        Library.jt = new JTable(tm);
    }
    public void print() {
        System.out.printf("Contact: id=%d, title=%s, author=%s, mediaType=%s, avail=%s, reserved %s\n", id, title, author, mediaType, avb, rsv);
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
