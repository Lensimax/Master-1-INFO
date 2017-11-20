package Entreprise;

public class Manutentionnaire_HR extends Manutentionnaire implements Haut_Risque {

    public Manutentionnaire_HR(String nom, String prenom,int nb){
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
