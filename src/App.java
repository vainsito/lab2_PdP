import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import feed.Article;
import feed.FeedParser;
import namedEntities.*;
import namedEntities.heuristics.CapitalizedWordHeuristic;
import utils.JSONFilter;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Config;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;

public class App {

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        List<String> entitiesJsonArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
            /////
            entitiesJsonArray = JSONFilter.parseJsonFilterData("src/data/dictionary.json", "label");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        run(config, feedsDataArray, entitiesJsonArray);
    }

    // TODO: Change the signature of this function if needed
    private static void run(Config config, List<FeedsData> feedsDataArray, List <String> entitiesJsonArray) {

        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }

        //Inicializacion de la lista de articulos
        List<Article> allArticles = new ArrayList<>();
        for(FeedsData feedData : feedsDataArray){
            try {
                String contenido = FeedParser.fetchFeed(feedData.getUrl());
                allArticles = FeedParser.parseXML(contenido);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        // Recorremos el array de feeds data para obtener el content xml
        // TODO: Populate allArticles with articles from corresponding feeds
        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            // TODO: Print the fetched feed
            for (Article article : allArticles) {
                article.prettyPrint();
            }
        }
/////////////////////////////////////////////////////////////////////////////////////////////////
        
        if (config.getComputeNamedEntities()) {
            // TODO: complete the message with the selected heuristic name
            System.out.println("Computing named entities using " + config.getHeuristicConfig());
            // TODO: compute named entities using the selected heuristic
            CapitalizedWordHeuristic heuristic = new CapitalizedWordHeuristic();
            List<String> candidatos = new ArrayList<>();
            // For para obtener los posibles Names Entities
            for (Article article : allArticles) { 
                candidatos = heuristic.extractCandidates(article.getDescription() + article.getTitle());
            }
            ////////////////////// A PARTIR DE ACA ES TERRENO NO EXPLORADO /////////////////////////////
            try{
                String content = new String(Files.readAllBytes(Paths.get("src/data/dictionary.json")), StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(content);
                // Recorro cada candidato de la lista de candidatos
                for (String candidate : candidatos){
                    // For para recorrer el json y poderme setear en la label correcta
                    for(int pos = 0; pos < jsonArray.length(); pos++){
                        // Convierto en json object para poder acceder a los datos de la pos
                        JSONObject jsonObject = jsonArray.getJSONObject(pos);
                        // Chequeo si el candidato esta en el json
                        if(jsonObject.getString("label") == candidate){
                            // Creo una entidad nombrada con su categoria y topicos correspondiente
                            NamedEntity namedEntity = new NamedEntity(new Category(jsonObject.getString("Category")),jsonObject.getString("label"));
                            // For para recorrer los topicos de la entidad y cargarlos
                            JSONArray topics_entity = jsonObject.getJSONArray("topics");
                            for(int i = 0; i < topics_entity.length(); i++){
                                namedEntity.addTopic(new Topics(topics_entity.getString(i)));
                            }
                            // Imprimo la entidad nombrada
                            namedEntity.namedEntityPrint();
                        }
                        // Si no esta en el json, se crea una entidad nombrada con categoria OTHER y topicos OTHER
                        else{
                            NamedEntity namedEntity = new NamedEntity(new Category("OTHER"),jsonObject.getString("label"));
                            namedEntity.addTopic(new Topics("OTHER"));

                            namedEntity.namedEntityPrint();
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            // TODO: Print stats
            System.out.println("\nStats: ");
            System.out.println("-".repeat(80));
        }
    }

    // TODO: Maybe relocate this function where it makes more sense
    private static void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        // TODO: Print the available heuristics with the following format
        System.out.println("                                       <name>: <description>");
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

}