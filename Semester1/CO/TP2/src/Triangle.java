

// TODO implem triangle

public class Triangle extends Forme {

    protected Point p1, p2, p3;

    public Triangle(Point p1, Point p2, Point p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public void angles(){

    }

    public boolean estTriRect(){
        return false;
    }

    public boolean estIsocele(){
        return false;
    }

    public boolean estEquilateral(){
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
