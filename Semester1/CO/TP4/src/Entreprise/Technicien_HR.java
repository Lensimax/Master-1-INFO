package Entreprise;

public class Technicien_HR extends Technicien implements Haut_Risque {

    public Technicien_HR(String nom, String prenom,int nb){
        super(nom, prenom, nb);
    }

    @Override
    public double prime() {
        return 50;
    }

    @Override
    public double calculerSalaire() {
        return super.calculerSalaire() + prime();
    }
}
