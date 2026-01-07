package controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.entitas.*;
import model.koneksi_DB;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.JTable;
import model.entitas;

public class controller_bankrut {

    // Fitur Nasabah
    
    public List<entitas.Nasabah> getAllNasabah() throws SQLException {
        List<entitas.Nasabah> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        String query = "SELECT * FROM nasabah";
        ResultSet rs = conn.createStatement().executeQuery(query);
        
        while(rs.next()) {
            list.add(new entitas.Nasabah(
                rs.getString("no_rekening"), 
                rs.getString("nama"), 
                rs.getString("jenis_rekening")
            ));
        }
        return list;
    }

    public void tambahNasabah(entitas.Nasabah n) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        
        // Validasi Duplikat No Rekening
        PreparedStatement cek = conn.prepareStatement("SELECT no_rekening FROM nasabah WHERE no_rekening=?");
        cek.setString(1, n.noRekening);
        if (cek.executeQuery().next()) {
            throw new SQLException("Nomor Rekening sudah terdaftar!");
        }

        PreparedStatement ps = conn.prepareStatement("INSERT INTO nasabah VALUES (?,?,?)");
        ps.setString(1, n.noRekening); 
        ps.setString(2, n.nama); 
        ps.setString(3, n.jenisRekening);
        ps.execute();
    }

    // Mengupdate data nasabah yang sudah ada
    public void ubahNasabah(entitas.Nasabah n) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE nasabah SET nama=?, jenis_rekening=? WHERE no_rekening=?"
        );
        ps.setString(1, n.nama); 
        ps.setString(2, n.jenisRekening); 
        ps.setString(3, n.noRekening);
        ps.executeUpdate();
    }

    // Menghapus data nasabah berdasarkan nomor rekening
    public void hapusNasabah(String noRekening) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM nasabah WHERE no_rekening=?");
        ps.setString(1, noRekening);
        ps.execute();
    }

    // Mencari nasabah berdasarkan keyword (nama atau no rekening)
     
    public List<entitas.Nasabah> cariNasabah(String key) throws SQLException {
        List<entitas.Nasabah> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM nasabah WHERE nama LIKE ? OR no_rekening LIKE ?"
        );
        ps.setString(1, "%" + key + "%"); 
        ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            list.add(new entitas.Nasabah(
                rs.getString("no_rekening"), 
                rs.getString("nama"), 
                rs.getString("jenis_rekening")
            ));
        }
        return list;
    }
    
    // Fitur Teller
    

    // Mengambil semua data teller dari database
     
    public List<entitas.Teller> getAllTeller() throws SQLException {
        List<entitas.Teller> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM teller");
        
        while(rs.next()) {
            list.add(new entitas.Teller(
                rs.getString("id_teller"), 
                rs.getString("nama"), 
                rs.getString("shift")
            ));
        }
        return list;
    }

    // Menambahkan teller baru ke database
     
    public void tambahTeller(entitas.Teller t) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        
        // Validasi Duplikat ID Teller
        PreparedStatement cek = conn.prepareStatement("SELECT id_teller FROM teller WHERE id_teller=?");
        cek.setString(1, t.idTeller);
        if (cek.executeQuery().next()) {
            throw new SQLException("ID Teller sudah terdaftar!");
        }
        
        PreparedStatement ps = conn.prepareStatement("INSERT INTO teller VALUES (?,?,?)");
        ps.setString(1, t.idTeller); 
        ps.setString(2, t.nama); 
        ps.setString(3, t.shift);
        ps.execute();
    }

    // Mengupdate data teller yang sudah ada
    public void ubahTeller(entitas.Teller t) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE teller SET nama=?, shift=? WHERE id_teller=?"
        );
        ps.setString(1, t.nama); 
        ps.setString(2, t.shift); 
        ps.setString(3, t.idTeller);
        ps.executeUpdate();
    }
