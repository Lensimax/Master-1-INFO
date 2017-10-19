package Forme;

public class Point {

    private double x,y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public String toString(){
        String renvoi = "";
        renvoi += "("+this.x +", "+this.y+")";
        return renvoi;
    }
}
