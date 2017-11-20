package Entreprise;

public class Vendeur extends Seller {

    public Vendeur(String nom, String prenom,int c){
        this.nom = nom;
        this.prenom = prenom;
        this.chiffre = c;
        this.prime = 100;
    }

    @Override
    public String getNom() {
        return "Le vendeur "+this.prenom+" "+this.nom;
    }
}
