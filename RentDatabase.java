package database;
import java.sql.*;
import javax.swing.JOptionPane;
public class RentDatabase {
	
	public static Connection Koneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3304/sewabuku", "root", "iyak004"); // uri, username, password
            return connection;
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Terkoneksi dengan database");
            return null;
        }
    }
}
