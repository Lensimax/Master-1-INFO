package Entreprise;

import java.util.ArrayList;

public class Personnel {

    ArrayList<Employe> personnel;

    public Personnel(){
        this.personnel = new ArrayList<>();
    }


    public void ajouterEmploye(Employe e){
        this.personnel.add(e);
    }

    public void calculerSalaires(){
        for(Employe e: this.personnel){
            System.out.println(e.calculerSalaire());
        }
    }

    public double salaireMoyen(){
        int avg = 0;

        for(Employe e: this.personnel){
            avg += e.calculerSalaire();
        }

        if(this.personnel.size() > 0){ // éviter division par 0
            return avg/this.personnel.size();
        } else {
            return -1;
        }


    }

    public void affiche(){
        for(Employe e: this.personnel){
            e.afficheToi();
        }
    }
}
