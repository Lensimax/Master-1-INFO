
// TODO implem parallelogramme

public class Parallelogramme extends Forme {

    protected Point p1, p2, p3, p4;

    public Parallelogramme(Point p1, Point p2, Point p3, Point p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
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
