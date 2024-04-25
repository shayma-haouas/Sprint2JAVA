package com.example.flo.entities;


import java.time.LocalDate;

public class Don {

    private int id;
    private String type;
    private String description;
    private LocalDate dateDon;

    private User user;


    public Don() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDon() {
        return dateDon;
    }

    public void setDateDon(LocalDate dateDon) {
        this.dateDon = dateDon;
    }

    public User getUser() {
        return user;
    }

    public void set(User user) {
        this.user = user;
    }


    public String toString() {
        return type;
    }
}