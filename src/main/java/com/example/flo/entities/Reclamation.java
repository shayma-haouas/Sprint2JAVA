package com.example.flo.entities;


import java.time.LocalDate;

public class Reclamation {

    private int id;
    private String type;
    private String description;
    private LocalDate dateajout;
    private LocalDate datemodif;
    
    private User user;
    

    public Reclamation() {}

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
    public LocalDate getDateajout() {
        return dateajout;
    }

    public void setDateajout(LocalDate dateajout) {
        this.dateajout = dateajout;
    }
    public LocalDate getDatemodif() {
        return datemodif;
    }

    public void setDatemodif(LocalDate datemodif) {
        this.datemodif = datemodif;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

    public String toString() {
        return type;
    }
}