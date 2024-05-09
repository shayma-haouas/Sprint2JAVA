package com.example.flo.entities;


import java.time.LocalDate;

<<<<<<< HEAD
public class Reponse implements Comparable<Reponse> {
=======
public class Reponse {
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c

    private int id;
    private String description;
    private LocalDate dateajout;
    private LocalDate datemodif;

    private Reclamation reclamation;


    public Reponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }


    public String toString() {
        return description;
    }
<<<<<<< HEAD
    public static String compareVar;

    public int compareTo(Reponse reponse) {
        return switch (compareVar) {
            case "Tri par description" -> reponse.getDescription().compareToIgnoreCase(description);
            case "Tri par date ajout" -> reponse.getDateajout().compareTo(dateajout);
            case "Tri par date modif" -> reponse.getDatemodif().compareTo(datemodif);
            default -> 0;
        };
    }
}

=======
}
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
