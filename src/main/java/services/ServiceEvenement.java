package services;

import entities.Evenement;
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
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getString("date_debut"),
                        rs.getString("date_fin"),
                        rs.getInt("nb_participant"),
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
}
