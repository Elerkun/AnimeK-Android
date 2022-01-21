package com.example.falle.myapplication;

/**
 * Created by Falle on 7/14/2018.
 */

public class Anime {
    int id;
    String en_jp;
    String ja_jp;
    String poster;
    String synopsis;
    String coverImage;
    String episode;
    String fechaEstreno;
    String fechaFin;
    String user;


    public Anime(int id, String en_jp, String ja_jp, String poster, String synopsis, String coverImage, String episode, String fechaEstreno, String fechaFin, String users) {
        this.id = id;
        this.en_jp = en_jp;
        this.ja_jp = ja_jp;
        this.poster = poster;
        this.synopsis = synopsis;
        this.coverImage = coverImage;
        this.episode = episode;
        this.fechaEstreno = fechaEstreno;
        this.fechaFin = fechaFin;
        this.user = users;
    }

    public Anime() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEn_jp() {
        return en_jp;
    }

    public void setEn_jp(String en_jp) {
        this.en_jp = en_jp;
    }

    public String getJa_jp() {
        return ja_jp;
    }

    public void setJa_jp(String ja_jp) {
        this.ja_jp = ja_jp;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}