package Transport;

import java.util.ArrayList;

/**
 * Created by thespygeek on 02/10/17.
 */

public class Cargaison {

    private float distance;
    private ArrayList<Marchandise> arrayMarchandise;

    public Cargaison(float distance){
        this.distance = distance;
        this.arrayMarchandise = new ArrayList<>();
    }

    public void ajout(Marchandise m){
        this.arrayMarchandise.add(m);
    }

    public void retrait(Marchandise m){
        this.arrayMarchandise.remove(m);
    }

    public void afficher(){
        for(Marchandise m : this.arrayMarchandise){
            System.out.println(m);
        }
    }

    public float volume_total(){
        float total = 0;
        for(Marchandise m : this.arrayMarchandise){
            total += m.getVolume();
        }

        return total;
    }

    public float poids_total(){
        float total = 0;
        for(Marchandise m : this.arrayMarchandise){
            total += m.getPoids();
        }

        return total;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public ArrayList<Marchandise> getArrayMarchandise() {
        return arrayMarchandise;
    }

    public void setArrayMarchandise(ArrayList<Marchandise> arrayMarchandise) {
        this.arrayMarchandise = arrayMarchandise;
    }
}
