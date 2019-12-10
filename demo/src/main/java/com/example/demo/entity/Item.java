package com.example.demo.entity;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private int itemid;
    private String releaserid;
    private String price;
    private String comment;
    private String detail;
    private String releasetime;
    private byte[] image;
    private String exist;
    private String kind;
    private String title;
    private String buyer;
    private String collectorid;
    private User releaser;

    public Item() {

    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getReleaserid() {
        return releaserid;
    }

    public void setReleaserid(String releaserid) {
        this.releaserid = releaserid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getExist() {
        return exist;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getCollectorid() {
        return collectorid;
    }

    public void setCollectorid(String collectorid) {
        this.collectorid = collectorid;
    }

    public User getReleaser() {
        return releaser;
    }

    public void setReleaser(User releaser) {
        this.releaser = releaser;
    }
}
