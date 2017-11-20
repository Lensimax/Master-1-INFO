package Entreprise;

public abstract class Seller extends Employe {
    protected int chiffre;
    protected int prime;


    @Override
    public double calculerSalaire() {
        return this.chiffre * 0.2 + this.prime;
    }
}
