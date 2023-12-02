package com.example.apphydroscape_nocturnal;

public class ResepModels {
    private String _id;
    private String image;
    private String resep;
    private String deskripsi;

    public ResepModels(){

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

//    public String get_pic() {
//        return image;
//    }
//
//    public void set_pic(String image) {
//        this.image = image;
//    }

    public String getResep() {
        return resep;
    }

    public void setResep(String resep) {
        this.resep = resep;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
