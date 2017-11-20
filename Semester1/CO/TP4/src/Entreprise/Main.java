package Entreprise;


public class Main {

    public static void main(String [] args){

        Personnel personnel = new Personnel();

        personnel.ajouterEmploye(new Vendeur("Business", "Pierre", 7000));
        personnel.ajouterEmploye(new Representant("Ok", "Bertrand", 8000));
        personnel.ajouterEmploye(new Technicien("Jordan", "Micheal", 94));

        personnel.affiche();

    }

}
