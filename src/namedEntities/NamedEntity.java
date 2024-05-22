package namedEntities;
import java.util.List;
import java.util.ArrayList;

public class NamedEntity {
    private String entidad_nombrada;
    private Category category;
    private List<Topics> topics;

    // Constructor 
    public NamedEntity(Category category, String palabra) {
        this.category = category;
        this.topics = new ArrayList<>();
        this.entidad_nombrada = palabra;
    }

    // Setter
    public void addTopic(Topics topic) {
        this.topics.add(topic);
    }
    // Getters
    public Category getCategory() {
        return category;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    // Metodo para imprimir
    public void namedEntityPrint(){
        System.out.println("Named Entity: " + this.entidad_nombrada);
        this.category.categoriePrint();
        for (Topics topic : this.topics){
            topic.topicPrint();
        }
    }
}