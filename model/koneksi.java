/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dhiaulhaqlaturua
 */
public class koneksi {
    
     private static Connection koneksi;

    public static Connection getKoneksi() {
        try {
            if (koneksi == null) {
                String url = "jdbc:mysql://localhost:3306/Manajemen_Data_Karyawan";
                String user = "root";
                String pass = "";
                koneksi = DriverManager.getConnection(url, user, pass);
            }
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return koneksi;
    }
}
