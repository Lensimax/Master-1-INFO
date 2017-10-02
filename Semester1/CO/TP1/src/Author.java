


public class Author {
    private final String firstName;
    private final String lastName;

    /**
     * @author thespygeek
     * @param firstName String du prenom
     * @param lastName String du nom de famille
     */

    public Author(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     *
     * @param o objet a comparer avec celui de la classe (this)
     * @return true si les deux attributs des deux objets sont égaux
     */

    public boolean equals(Object o){
        if(!(o instanceof Author)){
            return false;
        }

        Author author = (Author)o;
        return (author.firstName.equals(this.firstName) && author.lastName.equals(this.lastName));
    }
}