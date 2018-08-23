package com.example.falle.myapplication;



public class Episode {

    int season;
    int number;
    String synopsis;
    String en_jp;
    String thumbnail;
    String fechaEstreno;

    public Episode(int season, int number, String synopsis, String en_jp, String thumbnail, String fechaEstreno) {
        this.season = season;
        this.number = number;
        this.synopsis = synopsis;
        this.en_jp = en_jp;
        this.thumbnail = thumbnail;
        this.fechaEstreno = fechaEstreno;
    }

    public Episode() {
        season=0;
        number=0;
        synopsis=null;
        en_jp=null;
        thumbnail=null;
        fechaEstreno=null;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getEn_jp() {
        return en_jp;
    }

    public void setEn_jp(String en_jp) {
        this.en_jp = en_jp;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }
}