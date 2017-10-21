package Echec;


public class Reine extends Roi {
    public Reine(Echiquier e, Case c, boolean b){
        this.echec = e;
        this.actual_case = c;
        this.isBlue = b;
    }
}
