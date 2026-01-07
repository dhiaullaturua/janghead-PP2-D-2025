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
        setTitle("SISTEM INFORMASI PERBANKAN (BANK-RUT)");
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

    private JPanel panelNasabah() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCari.setBorder(BorderFactory.createTitledBorder("Pencarian Data"));
        JButton bCari = new JButton("🔍 Cari");
        pCari.add(new JLabel("Cari Nama/No. Rek:")); 
        pCari.add(tCariNasabah); 
        pCari.add(bCari);

        JPanel pForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pForm.setBorder(BorderFactory.createTitledBorder("Form Data Nasabah"));
        pForm.add(new JLabel("No. Rekening:")); 
        pForm.add(tNoRek);
        pForm.add(new JLabel("Nama Nasabah:")); 
        pForm.add(tNamaNasabah);
        pForm.add(new JLabel("Jenis Rekening:")); 
        pForm.add(cbJenisRekening);

        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("💾 Simpan");
        JButton bUpd = new JButton("✏️ Update");
        JButton bHap = new JButton("🗑️ Hapus");
        JButton bPdf = new JButton("📄 Export PDF");
        JButton bClr = new JButton("🔄 Clear");
        
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblNasabah.setModel(modNasabah);
        tblNasabah.setRowHeight(25);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblNasabah), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateNasabahInput()) return;
            try {
                control.tambahNasabah(new entitas.Nasabah(
                    tNoRek.getText().trim(), 
                    tNamaNasabah.getText().trim(), 
                    (String) cbJenisRekening.getSelectedItem()
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Nasabah Berhasil Disimpan");
                loadNasabah(""); 
                clearNasabah();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateNasabahInput()) return;
            try {
                control.ubahNasabah(new entitas.Nasabah(
                    tNoRek.getText().trim(), 
                    tNamaNasabah.getText().trim(), 
                    (String) cbJenisRekening.getSelectedItem()
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Nasabah Berhasil Diupdate");
                loadNasabah(""); 
                clearNasabah();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bHap.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Yakin hapus data nasabah ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    control.hapusNasabah(tNoRek.getText().trim());
                    JOptionPane.showMessageDialog(this, "✅ Data Nasabah Berhasil Dihapus");
                    loadNasabah(""); 
                    clearNasabah();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

        bCari.addActionListener(e -> loadNasabah(tCariNasabah.getText()));
        
        bPdf.addActionListener(e -> {
            if (control.exportPDF(tblNasabah, "Data_Nasabah_Bank.pdf")) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Data berhasil di-export ke PDF!\nLokasi: Downloads/Data_Nasabah_Bank.pdf");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal export PDF!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        bClr.addActionListener(e -> clearNasabah());

        tblNasabah.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblNasabah.getSelectedRow();
                tNoRek.setText(modNasabah.getValueAt(r, 0).toString());
                tNamaNasabah.setText(modNasabah.getValueAt(r, 1).toString());
                cbJenisRekening.setSelectedItem(modNasabah.getValueAt(r, 2).toString());
                tNoRek.setEditable(false);
            }
        });

        return main;
    }

    private void loadNasabah(String key) {
        modNasabah.setRowCount(0);
        try {
            List<entitas.Nasabah> list = key.isEmpty() ? 
                control.getAllNasabah() : control.cariNasabah(key);
            for(entitas.Nasabah n : list) {
                modNasabah.addRow(new Object[]{n.noRekening, n.nama, n.jenisRekening});
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void clearNasabah() {
        tNoRek.setText(""); 
        tNamaNasabah.setText(""); 
        cbJenisRekening.setSelectedIndex(0); 
        tCariNasabah.setText("");
        tNoRek.setEditable(true);
    }
    
    private JPanel panelTeller() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCari.setBorder(BorderFactory.createTitledBorder("Pencarian Data"));
        JButton bCari = new JButton("🔍 Cari");
        pCari.add(new JLabel("Cari Nama/ID Teller:")); 
        pCari.add(tCariTeller); 
        pCari.add(bCari);

        JPanel pForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pForm.setBorder(BorderFactory.createTitledBorder("Form Data Teller"));
        pForm.add(new JLabel("ID Teller:")); 
        pForm.add(tIdTeller);
        pForm.add(new JLabel("Nama Teller:")); 
        pForm.add(tNamaTeller);
        pForm.add(new JLabel("Shift:")); 
        pForm.add(cbShift);

        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("💾 Simpan");
        JButton bUpd = new JButton("✏️ Update");
        JButton bHap = new JButton("🗑️ Hapus");
        JButton bPdf = new JButton("📄 Export PDF");
        JButton bClr = new JButton("🔄 Clear");
        
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblTeller.setModel(modTeller);
        tblTeller.setRowHeight(25);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblTeller), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateTellerInput()) return;
            try {
                control.tambahTeller(new entitas.Teller(
                    tIdTeller.getText().trim(), 
                    tNamaTeller.getText().trim(), 
                    (String) cbShift.getSelectedItem()
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Teller Berhasil Disimpan");
                loadTeller(""); 
                clearTeller();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateTellerInput()) return;
            try {
                control.ubahTeller(new entitas.Teller(
                    tIdTeller.getText().trim(), 
                    tNamaTeller.getText().trim(), 
                    (String) cbShift.getSelectedItem()
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Teller Berhasil Diupdate");
                loadTeller(""); 
                clearTeller();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });
        
        bHap.addActionListener(e -> { 
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Yakin hapus data teller ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try { 
                    control.hapusTeller(tIdTeller.getText().trim()); 
                    JOptionPane.showMessageDialog(this, "✅ Data Teller Berhasil Dihapus");
                    loadTeller(""); 
                    clearTeller();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bCari.addActionListener(e -> loadTeller(tCariTeller.getText()));
        
        bPdf.addActionListener(e -> {
            if (control.exportPDF(tblTeller, "Data_Teller_Bank.pdf")) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Data berhasil di-export ke PDF!\nLokasi: Downloads/Data_Teller_Bank.pdf");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal export PDF!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        bClr.addActionListener(e -> clearTeller());

        tblTeller.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblTeller.getSelectedRow();
                tIdTeller.setText(modTeller.getValueAt(r, 0).toString());
                tNamaTeller.setText(modTeller.getValueAt(r, 1).toString());
                cbShift.setSelectedItem(modTeller.getValueAt(r, 2).toString());
                tIdTeller.setEditable(false);
            }
        });

        return main;
    }

    private void loadTeller(String key) {
        modTeller.setRowCount(0);
        try {
            List<entitas.Teller> list = key.isEmpty() ? 
                control.getAllTeller() : control.cariTeller(key);
            for(entitas.Teller t : list) {
                modTeller.addRow(new Object[]{t.idTeller, t.nama, t.shift});
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void clearTeller() {
        tIdTeller.setText(""); 
        tNamaTeller.setText(""); 
        cbShift.setSelectedIndex(0); 
        tCariTeller.setText("");
        tIdTeller.setEditable(true);
    }

    private JPanel panelTransaksi() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCari.setBorder(BorderFactory.createTitledBorder("Pencarian Data"));
        JButton bCari = new JButton("🔍 Cari");
        pCari.add(new JLabel("Cari ID/Jenis:")); 
        pCari.add(tCariTransaksi); 
        pCari.add(bCari);
        
        JPanel pForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pForm.setBorder(BorderFactory.createTitledBorder("Form Transaksi"));
        pForm.add(new JLabel("ID Transaksi:")); 
        pForm.add(tIdTransaksi);
        pForm.add(new JLabel("Jenis Transaksi:")); 
        pForm.add(cbJenisTransaksi);
        pForm.add(new JLabel("Nominal (Rp):")); 
        pForm.add(tNominal);

        JPanel pBtn = new JPanel();
        JButton bSim = new JButton("💾 Simpan");
        JButton bUpd = new JButton("✏️ Update");
        JButton bHap = new JButton("🗑️ Hapus");
        JButton bPdf = new JButton("📄 Export PDF");
        JButton bClr = new JButton("🔄 Clear");
        
        pBtn.add(bSim); pBtn.add(bUpd); pBtn.add(bHap); pBtn.add(bPdf); pBtn.add(bClr);

        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pCari, BorderLayout.NORTH);
        pNorth.add(pForm, BorderLayout.CENTER);
        pNorth.add(pBtn, BorderLayout.SOUTH);

        tblTransaksi.setModel(modTransaksi);
        tblTransaksi.setRowHeight(25);
        main.add(pNorth, BorderLayout.NORTH);
        main.add(new JScrollPane(tblTransaksi), BorderLayout.CENTER);

        bSim.addActionListener(e -> {
            if (!validateTransaksiInput()) return;
            try {
                control.tambahTransaksi(new entitas.Transaksi(
                    tIdTransaksi.getText().trim(), 
                    (String) cbJenisTransaksi.getSelectedItem(), 
                    Double.parseDouble(tNominal.getText().trim())
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Transaksi Berhasil Disimpan");
                loadTransaksi(""); 
                clearTransaksi();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bUpd.addActionListener(e -> {
            if (!validateTransaksiInput()) return;
            try {
                control.ubahTransaksi(new entitas.Transaksi(
                    tIdTransaksi.getText().trim(), 
                    (String) cbJenisTransaksi.getSelectedItem(), 
                    Double.parseDouble(tNominal.getText().trim())
                ));
                JOptionPane.showMessageDialog(this, "✅ Data Transaksi Berhasil Diupdate");
                loadTransaksi(""); 
                clearTransaksi();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        bHap.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Yakin hapus data transaksi ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    control.hapusTransaksi(tIdTransaksi.getText().trim());
                    JOptionPane.showMessageDialog(this, "✅ Data Transaksi Berhasil Dihapus");
                    loadTransaksi(""); 
                    clearTransaksi();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

        bCari.addActionListener(e -> loadTransaksi(tCariTransaksi.getText())); 
        
        bPdf.addActionListener(e -> {
            if (control.exportPDF(tblTransaksi, "Data_Transaksi_Bank.pdf")) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Data berhasil di-export ke PDF!\nLokasi: Downloads/Data_Transaksi_Bank.pdf");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal export PDF!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        bClr.addActionListener(e -> clearTransaksi());

        tblTransaksi.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblTransaksi.getSelectedRow();
                tIdTransaksi.setText(modTransaksi.getValueAt(r, 0).toString());
                cbJenisTransaksi.setSelectedItem(modTransaksi.getValueAt(r, 1).toString());
                tNominal.setText(modTransaksi.getValueAt(r, 2).toString());
                tIdTransaksi.setEditable(false);
            }
        });

        return main;
    }

    private void loadTransaksi(String key) {
        modTransaksi.setRowCount(0);
        try {
            List<entitas.Transaksi> list = key.isEmpty() ? 
                control.getAllTransaksi() : control.cariTransaksi(key);
            for(entitas.Transaksi tr : list) {
                modTransaksi.addRow(new Object[]{
                    tr.idTransaksi, 
                    tr.jenisTransaksi, 
                    currencyFormat.format(tr.nominal)
                });
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void clearTransaksi() {
        tIdTransaksi.setText(""); 
        tNominal.setText(""); 
        cbJenisTransaksi.setSelectedIndex(0); 
        tCariTransaksi.setText("");
        tIdTransaksi.setEditable(true);
    }

    
}
