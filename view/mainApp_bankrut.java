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