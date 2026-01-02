/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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


/**
 *
 * @author dhiaulhaqlaturua
 */
public class controller_bankrut {
    
      // ============================================================
    // 1. FITUR NASABAH (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================
    
    /**
     * Mengambil semua data nasabah dari database
     * @return List berisi semua data nasabah
     */
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

    /**
     * Menambahkan nasabah baru ke database
     * @param n Objek Nasabah yang akan ditambahkan
     */
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

    /**
     * Mengupdate data nasabah yang sudah ada
     * @param n Objek Nasabah dengan data terbaru
     */
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

    /**
     * Menghapus data nasabah berdasarkan nomor rekening
     * @param noRekening Nomor rekening yang akan dihapus
     */
    public void hapusNasabah(String noRekening) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM nasabah WHERE no_rekening=?");
        ps.setString(1, noRekening);
        ps.execute();
    }

    /**
     * Mencari nasabah berdasarkan keyword (nama atau no rekening)
     * @param key Keyword pencarian
     * @return List nasabah yang sesuai dengan pencarian
     */
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
    
    // ============================================================
    // 2. FITUR TELLER (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================

    /**
     * Mengambil semua data teller dari database
     * @return List berisi semua data teller
     */
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

    /**
     * Menambahkan teller baru ke database
     * @param t Objek Teller yang akan ditambahkan
     */
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

    /**
     * Mengupdate data teller yang sudah ada
     * @param t Objek Teller dengan data terbaru
     */
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

    /**
     * Menghapus data teller berdasarkan ID
     * @param idTeller ID Teller yang akan dihapus
     */
    public void hapusTeller(String idTeller) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM teller WHERE id_teller=?");
        ps.setString(1, idTeller);
        ps.execute();
    }

    /**
     * Mencari teller berdasarkan keyword (nama atau ID)
     * @param key Keyword pencarian
     * @return List teller yang sesuai dengan pencarian
     */
    public List<entitas.Teller> cariTeller(String key) throws SQLException {
        List<entitas.Teller> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM teller WHERE nama LIKE ? OR id_teller LIKE ?"
        );
        ps.setString(1, "%" + key + "%"); 
        ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            list.add(new entitas.Teller(
                rs.getString("id_teller"), 
                rs.getString("nama"), 
                rs.getString("shift")
            ));
        }
        return list;
    }
    
    // ============================================================
    // 3. FITUR TRANSAKSI (CREATE, READ, UPDATE, DELETE, SEARCH)
    // ============================================================

    /**
     * Mengambil semua data transaksi dari database
     * @return List berisi semua data transaksi
     */
    public List<entitas.Transaksi> getAllTransaksi() throws SQLException {
        List<entitas.Transaksi> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM transaksi");
        
        while(rs.next()) {
            list.add(new entitas.Transaksi(
                rs.getString("id_transaksi"), 
                rs.getString("jenis_transaksi"), 
                rs.getDouble("nominal")
            ));
        }
        return list;
    }

    /**
     * Menambahkan transaksi baru ke database
     * @param tr Objek Transaksi yang akan ditambahkan
     */
    public void tambahTransaksi(entitas.Transaksi tr) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        
        // Validasi Duplikat ID Transaksi
        PreparedStatement cek = conn.prepareStatement("SELECT id_transaksi FROM transaksi WHERE id_transaksi=?");
        cek.setString(1, tr.idTransaksi);
        if (cek.executeQuery().next()) {
            throw new SQLException("ID Transaksi sudah terdaftar!");
        }
        
        PreparedStatement ps = conn.prepareStatement("INSERT INTO transaksi VALUES (?,?,?)");
        ps.setString(1, tr.idTransaksi); 
        ps.setString(2, tr.jenisTransaksi); 
        ps.setDouble(3, tr.nominal);
        ps.execute();
    }

    /**
     * Mengupdate data transaksi yang sudah ada
     * @param tr Objek Transaksi dengan data terbaru
     */
    public void ubahTransaksi(entitas.Transaksi tr) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE transaksi SET jenis_transaksi=?, nominal=? WHERE id_transaksi=?"
        );
        ps.setString(1, tr.jenisTransaksi); 
        ps.setDouble(2, tr.nominal); 
        ps.setString(3, tr.idTransaksi);
        ps.executeUpdate();
    }

    /**
     * Menghapus data transaksi berdasarkan ID
     * @param idTransaksi ID Transaksi yang akan dihapus
     */
    public void hapusTransaksi(String idTransaksi) throws SQLException {
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM transaksi WHERE id_transaksi=?");
        ps.setString(1, idTransaksi);
        ps.execute();
    }

    /**
     * Mencari transaksi berdasarkan keyword (ID atau jenis transaksi)
     * @param key Keyword pencarian
     * @return List transaksi yang sesuai dengan pencarian
     */
    public List<entitas.Transaksi> cariTransaksi(String key) throws SQLException {
        List<entitas.Transaksi> list = new ArrayList<>();
        Connection conn = koneksi_DB.configDB();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM transaksi WHERE jenis_transaksi LIKE ? OR id_transaksi LIKE ?"
        );
        ps.setString(1, "%" + key + "%"); 
        ps.setString(2, "%" + key + "%");
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            list.add(new entitas.Transaksi(
                rs.getString("id_transaksi"), 
                rs.getString("jenis_transaksi"), 
                rs.getDouble("nominal")
            ));
        }
        return list;
    }
    
    // ============================================================
    // FITUR EXPORT PDF
    // ============================================================
    
    /**
     * Export data dari JTable ke format PDF
     * @param table JTable yang akan di-export
     * @param filename Nama file PDF yang akan dibuat
     * @return true jika berhasil, false jika gagal
     */
    public boolean exportPDF(JTable table, String filename) {
        try {
            // Membuat dokumen PDF dengan ukuran A4 landscape
            Document doc = new Document(PageSize.A4.rotate());
            
            // Membuat file di direktori Downloads
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + "Downloads" + File.separator + filename;
            
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            
            // Menambahkan judul
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("LAPORAN DATA - SISTEM PERBANKAN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            doc.add(title);
            
            // Menambahkan tanggal export
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Paragraph date = new Paragraph("Tanggal Export: " + sdf.format(new Date()), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            doc.add(date);
            
            // Membuat tabel PDF
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            
            // Styling untuk header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            
            // Menambahkan header
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setBackgroundColor(new BaseColor(0, 123, 255)); // Warna biru bank
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }
            
            // Font untuk data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            
            // Menambahkan data dari tabel
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    
                    // Warna bergantian untuk baris
                    if (row % 2 == 0) {
                        cell.setBackgroundColor(new BaseColor(240, 248, 255)); // Alice blue
                    }
                    
                    pdfTable.addCell(cell);
                }
            }
            
            doc.add(pdfTable);
            
            // Menambahkan footer
            Paragraph footer = new Paragraph("\n\nTotal Data: " + table.getRowCount() + " record(s)", dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            doc.add(footer);
            
            doc.close();
            
            System.out.println("PDF berhasil dibuat di: " + filePath);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saat membuat PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
}
