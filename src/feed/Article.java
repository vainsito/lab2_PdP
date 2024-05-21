package feed;

public class Article {
    // Tenemos que agregar los atributos: title, description, pubDate, link.
    private String title;
    private String description;
    private String pubDate;
    private String link;
    
    //Constructor sin tipo de retorno
    public Article(String titulo, String description, String pubDate, String link){

        this.title = titulo;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }
    // Getters 
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getPubDate() {
        return this.pubDate;
    }
    
    public String getLink() {
        return this.link;
    }
    // Metodo para printear
    public void prettyPrint() {
        // TODO: Implementar el metodo prettyPrint
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Publication Date: " + pubDate);
        System.out.println("Link: " + link);
        System.out.println("*****************************************************************************************");
    }
}