package Forme;

// TODO implem triangle

public class Triangle extends Forme {

    protected Point p1, p2, p3;

    public Triangle(){
        this.p1 = new Point(0.0, 0.0);
        this.p2 = new Point(1.0, 1.0);
        this.p3 = new Point(-1.0, -1.0);
    }

    public Triangle(Point p1, Point p2, Point p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    Triangle(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y){
        this.p1 = new Point(p1x, p1y);
        this.p2 = new Point(p2x, p2y);
        this.p3 = new Point(p3x, p3y);
    }

    public double[] angles(){
        double []angle = new double[3];

        angle[1] = Math.atan(Lib_Math.distance(p1, p2) / Lib_Math.distance(p1, p3));
        angle[2] = Math.atan(Lib_Math.distance(p2, p1) / Lib_Math.distance(p2, p3));
        angle[3] = Math.atan(Lib_Math.distance(p3, p2) / Lib_Math.distance(p3, p1));

        return angle;
    }

    public boolean estTriRect(){
        double angle [] = this.angles();
        for(int i=0; i<3; i++){
            if(angle[i] == 90.0){
                return true;
            }
        }

        return false;
    }

    public boolean estIsocele(){
        double angle [] = this.angles();

        return angle[0] == angle[1] || angle[1] == angle[2] || angle[2] == angle[0];
    }

    public boolean estEquilateral(){
        double angle [] = this.angles();

        return angle[0] == angle[1] && angle[1] == angle[2];
    }

    public Point[] points(){
        Point []point = {p1, p2, p3};

        return point;
    }



    @Override
    public double aire() {

    }

    @Override
    public double perimetre() {
        return Lib_Math.distance(p1, p2) + Lib_Math.distance(p2, p3) + Lib_Math.distance(p3, p1);
    }

    @Override
    public String toString() {
        // FLEMME
        return null;
    }
}
