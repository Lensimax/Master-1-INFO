
// TODO implem disque

public class Disque extends Forme {

    protected Point centre;

    protected double rayon;

    public Disque(Point centre, double rayon){
        this.centre = centre;
        this.rayon = rayon;
    }

    public double rayon(){
        return this.rayon;
    }

    public double diametre(){
        return this.rayon * 2;
    }

    public boolean estDansDisque(){
        return false;
    }

    public boolean estSurLeCercle(){
        return false;
    }


    @Override
    public double aire() {
        return 0;
    }

    @Override
    public double perimetre() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
