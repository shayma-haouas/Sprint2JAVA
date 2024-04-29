package edu.esprit.flo.entities;


import java.time.LocalDate;

public class Commande {

    private int id;
    private float montant;
    private LocalDate datecmd;
    private String lieucmd;
    private int quantite;

    private User user;
    private Produit produit;


    public Commande() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public LocalDate getDatecmd() {
        return datecmd;
    }

    public void setDatecmd(LocalDate datecmd) {
        this.datecmd = datecmd;
    }

    public String getLieucmd() {
        return lieucmd;
    }

    public void setLieucmd(String lieucmd) {
        this.lieucmd = lieucmd;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }


    public String toString() {
        return lieucmd;
    }
}