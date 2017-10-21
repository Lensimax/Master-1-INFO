package Echec;

/**
 * Created by thespygeek on 21/10/17.
 */
public class Roi extends Piece {

    public Roi(Echiquier e, Case c, boolean b){
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
