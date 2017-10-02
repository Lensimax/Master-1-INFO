package Transport;

/**
 * Created by thespygeek on 02/10/17.
 */
public class Marchandise {

    private float volume;
    private float poids;

    public Marchandise(float volume, float poids){
        this.volume = volume;
        this.poids = poids;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }
}
