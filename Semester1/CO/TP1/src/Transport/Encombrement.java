package Transport;

/**
 * Created by thespygeek on 02/10/17.
 */
public class Encombrement {

    private float poids;
    private float volume;
    private float poids_max;
    private float volume_max;

    public Encombrement(float poids_max, float volume_max) {
        this.poids = 0;
        this.volume = 0;
        this.poids_max = poids_max;
        this.volume_max = volume_max;
    }

    public boolean peut_rajouter(float volume, float poids){
        return (this.volume + volume < this.volume_max) && (this.poids + poids < this.poids_max);
    }

    public void rajouter(float volume, float poids){
        this.volume += volume;
        this.poids += poids;
    }

    public boolean peut_enlever(float volume, float poids){
        return (this.volume - volume > 0) && (this.poids - poids > 0);
    }

    public void enlever(float volume, float poids){
        this.volume -= volume;
        this.poids -= poids;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPoids_max() {
        return poids_max;
    }

    public void setPoids_max(float poids_max) {
        this.poids_max = poids_max;
    }

    public float getVolume_max() {
        return volume_max;
    }

    public void setVolume_max(float volume_max) {
        this.volume_max = volume_max;
    }
}
