package Forme;


public class Main {

    public static void main(String[] args){
//  TODO  Auto­generated  method  stub
        Disque d=new Disque(10.0,20.0,10.0);
        TriangleIsocele t=new TriangleIsocele(1.0,1.0,0.0,2.0,60.0);
        Parallelogramme p=new Parallelogramme(1.0,1.0,90.0,2.0,1.0,90.0);
        Carre c=new Carre(2.0,3.0,45.0,5.0);
        double[] angles=t.angles();
        System.out.println(d+"␣aire:␣"+ d.aire()+ "␣perimetre:␣"+d.perimetre()+
                "␣rayon:␣"+d.rayon()+ "␣diametre:␣"+d.diametre());
        Point[] coord=p.points();
        System.out.println(coord[0]);
        System.out.println(coord[1]);
        System.out.println(coord[2]);
        System.out.println(coord[3]);
        System.out.println(t.aire());
        System.out.println(angles[0]);
        System.out.println(angles[1]);
        System.out.println(angles[2]);
        System.out.println(t.estTriRect());
        System.out.println(t.estIsocele());
        System.out.println(t.estEquilateral());
        System.out.println(c.perimetre());
        System.out.println(c.aire());
        System.out.println(c);
    }
}
