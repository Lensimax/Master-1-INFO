package Forme;

/**
 * Created by thespygeek on 16/10/17.
 */
public class Lib_Math {

    public static double distance(Point p1, Point p2){

        return Math.sqrt( Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));

    }
}
