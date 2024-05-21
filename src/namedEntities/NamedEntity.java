package namedEntities;
import java.util.ArrayList;
import java.util.List;

// Clase madre
public class Categories{
    private List<String> topic_list; // Aca si topics es una clase seria List<Topics> 
    private String name_categorie;
    // Constructor
    public Categories(List<String> topic_list, String categorie_name){
        this.topic_list = topic_list;
        this.name_categorie = categorie_name;
    }
    // Getters
    public List<String> getTopic_list(){
        return this.topic_list;
    }
    public String getname_categorie(){
        return this.name_categorie;
    }

    // Metodo para agregar un topic a la lista
    public void addTopic(String topic){
        this.topic_list.add(topic);
    }

    // Metodo para eliminar un topic de la lista
    public void removeTopic(String topic){
        this.topic_list.remove(topic);
    }

    // Metodo para imprimir los atributos
    public void categoriePrint(){
        System.out.println("Categorie name: " + this.name_categorie);
        System.out.println("Topics: ");
        for (String topic : this.topic_list){
            System.out.println(topic);
        }
    }
}


// Posible idea para topic si la hacemos clase y cada topico una instancia de la clase
/* public class Topics {
    
    private List<String> topics_list;
    
    // Constructor
    public Topics(List<String> topics_list, ){
        this.topics_list = topics_list;
    }

} */


public class Person extends Categories{

    private String person_name;
    private Int person_age;
    private String person_job;

    public Person(String name, Int age, String job){
        this.person_name = name;
        this.person_age = age;
        this.person_job = job;
    }
    
    public String getPersonName(){
        return this.person_name;
    }

    public Int getPersonAge(){
        return this.person_age;
    }

    public String getPersonJob(){
        return this.person_job;
    }
    
}

public class Location extends Categories{

    private String location_Name;
    private Int location_longitud;
    private Int location_latitud;
    
    // Constructor
    public Location(String name, Int longitud, Int latitud){
        this.location_Name = name;
        this.location_longitud = longitud;
        this.location_latitud = latitud;
    }
    
    // Getters
    public String getLocationName(){
        return this.location_Name;
    }

    public Int getLocationLongitud(){
        return this.location_longitud;
    }

    public Int getLocationLatitud(){
        return this.location_latitud;
    }

    // Metodo para obtener el link de google maps de la locacion
}

public class Organization extends Categories {
    
    private String organization_name;
    private List<Strings> organization_members;
    
    public Organization (String name, List<Strings> members){
        this.organization_name = name;
        this.organization_members = members;
    }

    public String getOrganizationName(){
        return this.organization_name;
    }
     
    public String getOrganizationMembers(){
        return this.organization_members;
    }

}

public class Other extends Categories {

    private String other_name;

    public Other (String name){
        this.other_name = name;
    }
    
    public String getOtherName(){
        return this.other_name;
    }
     
}