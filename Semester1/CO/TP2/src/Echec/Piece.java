package Echec;


public abstract class Piece {

    protected Echiquier echec;
    protected Case actual_case;
    protected Boolean isBlue;

    public abstract String ToString();

    public Boolean isBlue() {
        return this.isBlue;
    }

    public Boolean isRed() {
        return !this.isBlue;
    }

    public abstract Boolean moveTo(Case dest);
}
