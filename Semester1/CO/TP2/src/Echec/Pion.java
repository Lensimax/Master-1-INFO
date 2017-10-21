package Echec;


public class Pion extends Piece {

    public Pion(Echiquier e, Case c, boolean b){
        this.echec = e;
        this.actual_case = c;
        this.isBlue = b;
    }

    @Override
    public String ToString() {
        return null;
    }

    @Override
    public Boolean moveTo(Case dest) {
        return null;
    }
}
