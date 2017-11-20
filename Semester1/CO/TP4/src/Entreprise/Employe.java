package Entreprise;

public abstract class Employe {
    protected String nom;
    protected String prenom;
    protected int age;
    protected String date_entree;

    public abstract double calculerSalaire();

    public abstract String getNom();

    public void afficheToi(){
        System.out.println(this.getNom()+" gagne "+this.calculerSalaire()+" euros");
    }

}
