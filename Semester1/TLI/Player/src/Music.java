
public class Music {
    String name;
    String auteur;
    String duree;

    Music(String n, String a, String d){
        this.name = n;
        this.auteur = a;
        this.duree = d;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}

