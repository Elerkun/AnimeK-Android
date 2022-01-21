package com.example.falle.myapplication;


public class Manga {
    int id;
    String en_jp;
    String en;
    String poster;
    String synopsis;
    String coverImage;
    String chapterCount;
    String startDate;
    String endDate;

    public Manga(int id, String en_jp, String en, String poster, String synopsis, String coverImage, String chapterCount, String startDate, String endDate) {
        this.id = id;
        this.en_jp = en_jp;
        this.en = en;
        this.poster = poster;
        this.synopsis = synopsis;
        this.coverImage = coverImage;
        this.chapterCount = chapterCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Manga() {
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

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
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

    public String getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(String chapterCount) {
        this.chapterCount = chapterCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
