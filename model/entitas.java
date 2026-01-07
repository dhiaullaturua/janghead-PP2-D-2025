/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rudolprasetyo
 */
public class entitas {
    
  // Entity Nasabah
    public static class Nasabah {
        public String noRekening;
        public String nama;
        public String jenisRekening;
        
        public Nasabah(String noRekening, String nama, String jenisRekening) { 
            this.noRekening = noRekening; 
            this.nama = nama; 
            this.jenisRekening = jenisRekening; 
        }
        
        // Getter methods
        public String getNoRekening() { return noRekening; }
        public String getNama() { return nama; }
        public String getJenisRekening() { return jenisRekening; }
        
        // Setter methods
        public void setNoRekening(String noRekening) { this.noRekening = noRekening; }
        public void setNama(String nama) { this.nama = nama; }
        public void setJenisRekening(String jenisRekening) { this.jenisRekening = jenisRekening; }
    }
    
    // Entity Teler
    public static class Teller {
        public String idTeller;
        public String nama;
        public String shift;
        
        public Teller(String idTeller, String nama, String shift) { 
            this.idTeller = idTeller; 
            this.nama = nama; 
            this.shift = shift; 
        }
        
        // Getter methods
        public String getIdTeller() { return idTeller; }
        public String getNama() { return nama; }
        public String getShift() { return shift; }
        
        // Setter methods
        public void setIdTeller(String idTeller) { this.idTeller = idTeller; }
        public void setNama(String nama) { this.nama = nama; }
        public void setShift(String shift) { this.shift = shift; }
    }
    
    // Entity Transaksi
    public static class Transaksi {
        public String idTransaksi;
        public String jenisTransaksi;
        public double nominal;
        
        public Transaksi(String idTransaksi, String jenisTransaksi, double nominal) { 
            this.idTransaksi = idTransaksi; 
            this.jenisTransaksi = jenisTransaksi; 
            this.nominal = nominal; 
        }
        
        // Getter methods
        public String getIdTransaksi() { return idTransaksi; }
        public String getJenisTransaksi() { return jenisTransaksi; }
        public double getNominal() { return nominal; }
        
        // Setter methods
        public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }
        public void setJenisTransaksi(String jenisTransaksi) { this.jenisTransaksi = jenisTransaksi; }
        public void setNominal(double nominal) { this.nominal = nominal; }
    }
}

