package services;

import entities.Evenement;
import entities.Sponsor;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceEvenement implements CRUDEvent<Evenement> {
    Connection connection;
    public ServiceEvenement(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override

    public void ajouterEvenement(Evenement r) {
        try {
            String requete1 = "INSERT INTO evenement(sponsor_id,nameevent,type, datedebut, datefin, description,nbparticipant, lieu, image) VALUES(?,?,?,STR_TO_DATE(?, '%d/%m/%Y'),STR_TO_DATE(?, '%d/%m/%Y'),?,?,?,?)";
            PreparedStatement pst =  MyDatabase.getInstance().getConnection().prepareStatement(requete1);
            pst.setInt(1, r.getSponsor_id());
            pst.setString(2, r.getNameevent());
            pst.setString(3, r.getType());
            pst.setString(4, r.getDatedebut());
            pst.setString(5, r.getDatefin());
            pst.setString(6, r.getDescription());
            pst.setInt(7, r.getNbparticipant());
            pst.setString(8, r.getLieu());
            pst.setString(9, r.getImage());

            pst.executeUpdate();
            System.out.println("Evenement ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void supprimerEvenement(int id) {
        try {
            String requete = "DELETE FROM evenement WHERE id=?";
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Evenement supprimé avec succès !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void modifierEvenement(Evenement r) {
        try {
            String requete = "UPDATE evenement SET sponsor_id = ?, nameevent = ?, type= ?, datedebut= STR_TO_DATE(?, '%d/%m/%Y'), datefin = STR_TO_DATE(?, '%d/%m/%Y'), description =? , nbparticipant = ?, lieu=? , image = ? WHERE id = ?";
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, r.getSponsor_id());
            pst.setString(2, r.getNameevent());
            pst.setString(3, r.getType());
            pst.setString(4, r.getDatedebut());
            pst.setString(5, r.getDatefin());
            pst.setString(6, r.getDescription());
            pst.setInt(7, r.getNbparticipant());
            pst.setString(8, r.getLieu());
            pst.setString(9, r.getImage());
            pst.setInt(10, r.getId());
            pst.executeUpdate();
            System.out.println("Evenement modifié avec succès !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public List<Evenement> afficherEV() {
        List<Evenement> liste = new ArrayList<>();
        try {
            String requete = "SELECT * FROM evenement";
            Statement st = MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Evenement evenement;
                evenement = new Evenement(
                        rs.getInt("id"),
                        rs.getInt("sponsor_id"),
                        rs.getString("nameevent"),
                        rs.getString("type"),
                        rs.getString("datedebut"),
                        rs.getString("datefin"),
                        rs.getString("description"),
                        rs.getInt("nbparticipant"),
                        rs.getString("lieu"),
                        rs.getString("image")


                );
                liste.add(evenement);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return liste;
    }




    public Sponsor getSponsorById(int id) {
        Sponsor sponsor = null;
        try {
            Connection conn = MyDatabase.getInstance().getConnection();
            String query = "SELECT * FROM sponsor WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                sponsor = new Sponsor();
                sponsor.setId(rs.getInt("id"));
                sponsor.setName(rs.getString("name"));
                sponsor.setEmail(rs.getString("email"));
                // Compléter avec les autres champs
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sponsor;
    }
    public String getNomSponsor(Sponsor sponsor) {
        String nomSponsor = null;
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            String query = "SELECT name FROM sponsor WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sponsor.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nomSponsor = resultSet.getString("name");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nomSponsor;
    }
    public Integer getIdSponsorFromNom(Sponsor sponsor) {
        Integer id = null;
        try (PreparedStatement pstmt = MyDatabase.getInstance().getConnection().prepareStatement("SELECT id  FROM Sponsor WHERE nom_sponsor= ?")) {
            pstmt.setString(1, sponsor.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("ID du sponsor : " + id);
        return id;
    }

    public String getNomSponsor(int id) {
        String nom = "";
        try (PreparedStatement pstmt = MyDatabase.getInstance().getConnection().prepareStatement("SELECT name FROM sponsor WHERE id= ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nom = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return nom;
    }
    public List<Evenement> afficherEVWithSponsors() {
        List<Evenement> evenements = new ArrayList<>();
        try {
            Connection conn = MyDatabase.getInstance().getConnection();
            String query = "SELECT e.*, s.* FROM evenement e JOIN sponsor s ON e.sponsor_id = s.id";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setId(rs.getInt("s.id"));
                sponsor.setName(rs.getString("s.name"));
                // Populate other sponsor fields if needed

                Evenement evenement = new Evenement();
                evenement.setId(rs.getInt("e.id"));
                evenement.setSponsor_id(rs.getInt("e.sponsor_id"));
                evenement.setNameevent(rs.getString("e.nameevent"));

                evenement.setType(rs.getString("e.type"));

                evenement.setDatedebut(rs.getString("e.datedebut"));
                evenement.setDatefin(rs.getString("e.datefin"));
                evenement.setDescription(rs.getString("e.description"));
                evenement.setNbparticipant(rs.getInt("e.nbparticipant"));
                evenement.setLieu(rs.getString("e.lieu"));
                evenement.setImage(rs.getString("e.image"));

                evenement.setSponsor(sponsor);

                evenements.add(evenement);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }


    public String getSponsorNameById(int sponsorId) {
        String sponsorName = "";
        try {
            String requete = "SELECT name FROM sponsor WHERE id = ?";
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, sponsorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                sponsorName = rs.getString("name");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return sponsorName;
    }

    //for sponsor
    public int getIdSponsor(String nom) {
        int id = 0;
        try (PreparedStatement pstmt = MyDatabase.getInstance().getConnection().prepareStatement("SELECT id FROM sponsor WHERE name= ?")) {
            pstmt.setString(1, nom);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
}
