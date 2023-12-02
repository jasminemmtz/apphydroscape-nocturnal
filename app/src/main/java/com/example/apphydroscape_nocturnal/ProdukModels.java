package com.example.apphydroscape_nocturnal;

public class ProdukModels {
    private String _id;
    private String image;
    private String nama_produk;
    private Integer harga_produk;
    private String kategori;
    private Integer berat;
    private String daya_tahan;
    private String gizi;
    private String cara_menyimpan;

    public ProdukModels() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_pic() {
        return image;
    }

    public void set_pic(String image) {
        this.image = image;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public Integer getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(Integer harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Integer getBerat() {
        return berat;
    }

    public void setBerat(Integer berat) {
        this.berat = berat;
    }

    public String getDaya_tahan() {
        return daya_tahan;
    }

    public void setDaya_tahan(String daya_tahan) {
        this.daya_tahan = daya_tahan;
    }

    public String getGizi() {
        return gizi;
    }

    public void setGizi(String gizi) {
        this.gizi = gizi;
    }

    public String getCara_menyimpan() {
        return cara_menyimpan;
    }

    public void setCara_menyimpan(String cara_menyimpan) {
        this.cara_menyimpan = cara_menyimpan;
    }
}
