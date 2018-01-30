import java.util.*;

public class Registry implements Registry_itf{
    private ArrayList<Person> personList;

    public Registry(){
        personList = new ArrayList<>();
    }

    public void add(Person p){
        personList.add(p);
    }

    public String getPhone(String name){
        Person p = search(name);
        if (p != null){
            return p.getName();
        } else {
            return null;
        }
    }

    public Iterable<Person> getAll(){
        return new Iterable<Person>(){
            public Iterator<Person> iterator(){
                return personList.iterator();
            }
        };
    }

    public Person search(String name){
        for(Person p: personList){
            if (p.getName() == name){
                return p;
            }
        }
        return null;
    }

    public String toString(){

        String reg = "";
        for(Person p: personList){
            reg += p.toString() + "\n";
        }
        return reg;
    }

}
