import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String phone;

    public Person(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName(){
        return this.name;
    }

    public String getPhone(){
        return this.phone;
    }

    public String toString(){
        String pers = "";
        pers += "name: "+name + ", ";
        pers += "phone: " + phone;

        return pers;
    }

}
