package Entreprise;

public class Manutentionnaire extends Employe {

    int nb_heures;

    public Manutentionnaire(String nom, String prenom,int nb){
        this.nom = nom;
        this.prenom = prenom;
        this.nb_heures = nb;
    }

    @Override
    public double calculerSalaire() {
        return nb_heures * 10;
    }

    @Override
    public String getNom() {
        return "Le manutentionnaire "+this.prenom+" "+this.nom;
    }
}
