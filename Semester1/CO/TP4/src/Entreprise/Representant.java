package Entreprise;

public class Representant extends Seller {

    public Representant(String nom, String prenom,int c){
        this.nom = nom;
        this.prenom = prenom;
        this.chiffre = c;
        this.prime = 150;
    }

    @Override
    public String getNom(){
        return "Le representant "+this.prenom+" "+this.nom;
    }
}
