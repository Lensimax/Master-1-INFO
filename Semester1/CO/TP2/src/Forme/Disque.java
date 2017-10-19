package Forme;

public class Disque extends Forme {

    protected Point centre;

    protected double rayon;

    public Disque(Point centre, double rayon){
        this.centre = centre;
        this.rayon = rayon;
    }

    public Disque(double x, double y, double rayon){
        this.centre = new Point(x, y);
        this.rayon = rayon;
    }

    public double rayon(){
        return this.rayon;
    }

    public double diametre(){
        return this.rayon * 2;
    }

    public boolean estDansDisque(double x, double y){
        double distance_au_centre = Lib_Math.distance(new Point(x, y), centre);

        return distance_au_centre <= rayon;
    }

    public boolean estSurLeCercle(double x, double y){
        double distance_au_centre = Lib_Math.distance(new Point(x, y), centre);

        return distance_au_centre == rayon;
    }


    @Override
    public double aire() {
        return Math.PI * this.rayon * this.rayon;
    }

    @Override
    public double perimetre() {
        return 2 * Math.PI * this.rayon;
    }

    @Override
    public String toString() {
        String renvoi = "";
        renvoi += "Forme.Disque: \nCentre: "+this.centre+"\nRayon: "+this.rayon;
        return renvoi;
    }
}
