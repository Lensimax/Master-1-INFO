package Forme;


public abstract class Forme {

    protected Point p;

    Forme(){
        this.p = new Point(0,0);
    }

    Forme(Point p){
        this.p = p;
    }

    Forme(double x, double y){
        this.p = new Point(x, y);
    }

    public abstract double aire();
    public abstract double perimetre();
    public abstract String toString();
}
