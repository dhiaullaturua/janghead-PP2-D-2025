/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;



/**
 *
 * @author dhiaulhaqlaturua
 */

import config.koneksi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormKaryawan extends JFrame {

    // ===== KOMPONEN =====
    private JTextField txtNama, txtJabatan, txtGaji, txtNoHp;
    private JButton btnSimpan, btnUbah, btnHapus;
    private JTable tableKaryawan;

    public FormKaryawan() {
        setTitle("Manajemen Data Karyawan");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        tampilData();
    }

    // =========================
    // MEMBANGUN UI MANUAL
    // =========================
    private void initUI() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));

        panelForm.add(new JLabel("Nama"));
        txtNama = new JTextField();
        panelForm.add(txtNama);

        panelForm.add(new JLabel("Jabatan"));
        txtJabatan = new JTextField();
        panelForm.add(txtJabatan);

        panelForm.add(new JLabel("Gaji"));
        txtGaji = new JTextField();
        panelForm.add(txtGaji);

        panelForm.add(new JLabel("No HP"));
        txtNoHp = new JTextField();
        panelForm.add(txtNoHp);

        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");

        JPanel panelButton = new JPanel();
        panelButton.add(btnSimpan);
        panelButton.add(btnUbah);
        panelButton.add(btnHapus);

        tableKaryawan = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableKaryawan);

        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // EVENT
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());

        tableKaryawan.getSelectionModel().addListSelectionListener(e -> isiForm());
    }

    // =========================
    // READ
    // =========================
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nama", "Jabatan", "Gaji", "No HP"}, 0);

        try {
            Connection conn = koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM karyawan");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_karyawan"),
                    rs.getString("nama"),
                    rs.getString("jabatan"),
                    rs.getDouble("gaji"),
                    rs.getString("no_hp")
                });
            }
            tableKaryawan.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // =========================
    // CREATE
    // =========================
    private void simpan() {
        if (txtNama.getText().isEmpty() ||
            txtJabatan.getText().isEmpty() ||
            txtGaji.getText().isEmpty() ||
            txtNoHp.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Semua field wajib diisi");
            return;
        }

        try {
            Double.parseDouble(txtGaji.getText());

            String sql = "INSERT INTO karyawan VALUES (NULL, ?, ?, ?, ?)";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtJabatan.getText());
            ps.setDouble(3, Double.parseDouble(txtGaji.getText()));
            ps.setString(4, txtNoHp.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            tampilData();
            resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gaji harus angka");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // =========================
    // UPDATE
    // =========================
    private void ubah() {
        int row = tableKaryawan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu");
            return;
        }

        try {
            int id = Integer.parseInt(tableKaryawan.getValueAt(row, 0).toString());
            String sql = "UPDATE karyawan SET nama=?, jabatan=?, gaji=?, no_hp=? WHERE id_karyawan=?";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setString(2, txtJabatan.getText());
            ps.setDouble(3, Double.parseDouble(txtGaji.getText()));
            ps.setString(4, txtNoHp.getText());
            ps.setInt(5, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            tampilData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // =========================
    // DELETE
    // =========================
    private void hapus() {
        int row = tableKaryawan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tableKaryawan.getValueAt(row, 0).toString());
                PreparedStatement ps = koneksi.getKoneksi()
                        .prepareStatement("DELETE FROM karyawan WHERE id_karyawan=?");
                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                tampilData();
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void isiForm() {
        int row = tableKaryawan.getSelectedRow();
        if (row >= 0) {
            txtNama.setText(tableKaryawan.getValueAt(row, 1).toString());
            txtJabatan.setText(tableKaryawan.getValueAt(row, 2).toString());
            txtGaji.setText(tableKaryawan.getValueAt(row, 3).toString());
            txtNoHp.setText(tableKaryawan.getValueAt(row, 4).toString());
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtJabatan.setText("");
        txtGaji.setText("");
        txtNoHp.setText("");
    }
}

