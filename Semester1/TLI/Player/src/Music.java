
public class Music {
    String Name;
    String Auteur;
    String Duree;

    Music(String name_music, String artist, String length){
        this.Name = name_music;
        this.Auteur = artist;
        this.Duree = length;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuteur() {
        return Auteur;
    }

    public void setAuteur(String auteur) {
        Auteur = auteur;
    }

    public String getDuree() {
        return Duree;
    }

    public void setDuree(String duree) {
        Duree = duree;
    }
}

