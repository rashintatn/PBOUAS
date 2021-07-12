package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Rent;

public class RentRepository {
	public static Statement st;
	public static PreparedStatement pst;
    public static ResultSet rs;
    static Connection cn = database.RentDatabase.Koneksi();
    
	public static  ArrayList<Rent> fetchRents() {
		ArrayList<Rent> rentList = new ArrayList<Rent>();
		try {
			st = cn.createStatement();
			rs = st.executeQuery("SELECT * FROM sewabuku");
			 while (rs.next()) {
				 Rent rentData = new Rent();
				 rentData.id = rs.getInt("id");
				 rentData.judul = rs.getString("judul");
				 rentData.tanggalPinjam = rs.getDate("tanggal_harus_kembali") != null ? rs.getDate("tanggal_pinjam").toLocalDate() : null;
				 rentData.tanggalHarusKembali = rs.getDate("tanggal_harus_kembali") != null ? rs.getDate("tanggal_harus_kembali").toLocalDate() : null;
				 rentData.tanggalKembali = rs.getDate("tanggal_kembali") != null ? rs.getDate("tanggal_kembali").toLocalDate() : null;
				 rentData.biayaSewa = rs.getInt("biaya_sewa");
				 rentData.denda = rs.getInt("denda");
			     rentList.add(rentData);
			    }
		} catch(Exception e) {
		    e.printStackTrace();
		}
		
		return rentList;
		
	}
	public static int addRent(String title) {
		LocalDate tanggalPinjam = LocalDate.now();
		LocalDate tanggalHarusKembali = LocalDate.now().plusDays(7);
		try {
			pst = cn.prepareStatement("INSERT INTO sewabuku (judul, tanggal_pinjam, tanggal_harus_kembali) VALUES ('" + title + "', '" + tanggalPinjam +"', '" + tanggalHarusKembali+ "');");
			pst.execute();
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
		    return 0;
		}
	}
	public static int removeRent(String rentId) {
		try {
			pst = cn.prepareStatement("DELETE FROM sewabuku WHERE id="+ rentId +";");
			pst.execute();
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
		    return 0;
		}
	}
	public static int returnRent(String rentId, LocalDate tanggalHarusKembali) {
		LocalDate tanggalKembali = LocalDate.now();
		int keterlambatan = tanggalKembali.compareTo(tanggalHarusKembali);
		int denda = 0;
		if (keterlambatan > 0) {
			denda = 2000 * keterlambatan;
		}
		try {
			pst = cn.prepareStatement("UPDATE sewabuku SET tanggal_kembali = '" + tanggalKembali + "', denda = '" + denda + "', biaya_sewa = 5000 WHERE id = " + rentId + ";");
			pst.execute();
			return 1;
		}catch(Exception e) {
			return 0;
		}
	}
	public static int updateBookTitle(String rentId, String bookTitle) {
		try {
			pst = cn.prepareStatement("UPDATE sewabuku SET judul = '" + bookTitle + "' WHERE id = " + rentId + ";");
			pst.execute();
			return 1;
		}catch(Exception e) {
			return 0;
		}
	}
}
