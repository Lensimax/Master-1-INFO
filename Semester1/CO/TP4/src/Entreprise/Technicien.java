package Entreprise;

public class Technicien extends Employe {

    int nb_piece_produites;
    final int prix_piece = 10;

    public Technicien(String nom, String prenom,int nb){
        this.nom = nom;
        this.prenom = prenom;
        this.nb_piece_produites = nb;
    }

    @Override
    public double calculerSalaire() {
        return prix_piece * nb_piece_produites;
    }

    @Override
    public String getNom() {
        return "Le technicien "+this.prenom+" "+this.nom;
    }
}
