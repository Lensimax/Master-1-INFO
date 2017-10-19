package Forme;
// TODO implem parallelogramme

public class Parallelogramme extends Forme {

    protected Point p1, p2, p3, p4;

    public Parallelogramme(Point p1, Point p2, Point p3, Point p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public Parallelogramme(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, double p4x, double p4y){
        this.p1 = new Point(p1x, p1y);
        this.p2 = new Point(p2x, p2y);
        this.p3 = new Point(p3x, p3y);
        this.p4 = new Point(p4x, p4y);
    }

    public boolean estLosange(){
        return false;
    }

    public boolean estRectangle(){
        return false;
    }

    public boolean estCarre(){
        return false;
    }

    public Point[] points(){
        return null;
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
