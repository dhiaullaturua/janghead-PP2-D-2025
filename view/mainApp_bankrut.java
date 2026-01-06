package view;

import controller.controller_bankrut;
import model.entitas.Nasabah;
import model.entitas.Teller;
import model.entitas.Transaksi;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.Locale;
import model.entitas;


public class mainApp_bankrut extends JFrame  {
    
    private controller_bankrut control = new controller_bankrut();
    
    private JTabbedPane tabs = new JTabbedPane();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    private JTextField tNoRek = new JTextField();
    private JTextField tNamaNasabah = new JTextField();
    private JTextField tCariNasabah = new JTextField(15);
    private JComboBox<String> cbJenisRekening = new JComboBox<>(new String[]{
        "Tabungan", "Giro", "Deposito", "Tabungan Berjangka"
    });
    private JTable tblNasabah = new JTable();
    private DefaultTableModel modNasabah = new DefaultTableModel(
        new Object[]{"No. Rekening", "Nama Nasabah", "Jenis Rekening"}, 0
    );
    
    private JTextField tIdTeller = new JTextField();
    private JTextField tNamaTeller = new JTextField();
    private JTextField tCariTeller = new JTextField(15);
    private JComboBox<String> cbShift = new JComboBox<>(new String[]{
        "Pagi (08:00-14:00)", "Siang (14:00-20:00)", "Full Time (08:00-16:00)"
    });
    private JTable tblTeller = new JTable();
    private DefaultTableModel modTeller = new DefaultTableModel(
        new Object[]{"ID Teller", "Nama", "Shift"}, 0
    );
    
    private JTextField tIdTransaksi = new JTextField();
    private JTextField tNominal = new JTextField();
    private JTextField tCariTransaksi = new JTextField(15);
    private JComboBox<String> cbJenisTransaksi = new JComboBox<>(new String[]{
        "Setor Tunai", "Tarik Tunai", "Transfer", "Pembayaran"
    });
    private JTable tblTransaksi = new JTable();
    private DefaultTableModel modTransaksi = new DefaultTableModel(
        new Object[]{"ID Transaksi", "Jenis Transaksi", "Nominal"}, 0
    );
    

    public mainApp_bankrut() {
        setTitle("SISTEM INFORMASI PERBANKAN - Bank Digital Indonesia");
        setSize(1000, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs.addTab("📋 Data Nasabah", panelNasabah());
        tabs.addTab("👤 Data Teller", panelTeller());
        tabs.addTab("💰 Transaksi", panelTransaksi());
        add(tabs);

        loadNasabah("");
        loadTeller("");
        loadTransaksi("");
    }

    private boolean validateNasabahInput() {
        String noRek = tNoRek.getText().trim();
        String nama = tNamaNasabah.getText().trim();
        String jenisRek = (String) cbJenisRekening.getSelectedItem();

        if (noRek.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nomor Rekening dan Nama wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!noRek.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, 
                "Nomor Rekening harus berupa angka!\nContoh: 1234567890", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNoRek.requestFocus();
            return false;
        }

        if (noRek.length() < 10) {
            JOptionPane.showMessageDialog(this, 
                "Nomor Rekening minimal 10 digit!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNoRek.requestFocus();
            return false;
        }

        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "Nama harus berupa huruf!\nContoh: Ahmad Santoso", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNamaNasabah.requestFocus();
            return false;
        }

        if (jenisRek == null || jenisRek.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Jenis Rekening harus dipilih!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    private boolean validateTellerInput() {
        String idTeller = tIdTeller.getText().trim();
        String nama = tNamaTeller.getText().trim();
        String shift = (String) cbShift.getSelectedItem();

        if (idTeller.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "ID Teller dan Nama wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!idTeller.matches("[A-Z0-9]+")) {
            JOptionPane.showMessageDialog(this, 
                "ID Teller harus berupa huruf kapital dan/atau angka!\nContoh: TLR001 atau T001", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tIdTeller.requestFocus();
            return false;
        }

        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "Nama harus berupa huruf!\nContoh: Budi Setiawan", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNamaTeller.requestFocus();
            return false;
        }

        if (shift == null || shift.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Shift harus dipilih!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    private boolean validateTransaksiInput() {
        String idTransaksi = tIdTransaksi.getText().trim();
        String nominalStr = tNominal.getText().trim();
        String jenisTransaksi = (String) cbJenisTransaksi.getSelectedItem();

        if (idTransaksi.isEmpty() || nominalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "ID Transaksi dan Nominal wajib diisi!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!nominalStr.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(this, 
                "Nominal harus berupa angka!\nContoh: 100000 atau 50000.50", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNominal.requestFocus();
            return false;
        }

        double nominal = Double.parseDouble(nominalStr);
        if (nominal <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Nominal harus lebih besar dari 0!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            tNominal.requestFocus();
            return false;
        }

        if (jenisTransaksi == null || jenisTransaksi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Jenis Transaksi harus dipilih!", 
                "Error Validasi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }