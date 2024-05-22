import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                // Guardo la descripcion del articulo en un string
                candidatos.addAll(heuristic.extractCandidates(article.getDescription()));
            }
            ////////////////////// A PARTIR DE ACA ES TERRENO NO EXPLORADO /////////////////////////////
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/data/dictionary.json")), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);

            // Mapa para almacenar las entidades nombradas
            Map<String, NamedEntity> namedEntities = new HashMap<>();

            for (String candidate : candidatos) {
                boolean found = false;
                for (int pos = 0; pos < jsonArray.length(); pos++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(pos);
                    if (jsonObject.has("keywords")) {
                        JSONArray keywords = jsonObject.getJSONArray("keywords");
                        for (int i = 0; i < keywords.length(); i++) {
                            String keyword = keywords.getString(i);

                            if (keyword.equalsIgnoreCase(candidate)) {
                                NamedEntity namedEntity;
                                boolean isNewEntity = false;
                                if (namedEntities.containsKey(candidate)) {
                                    // Si la entidad ya existe, incrementa el campo repetitions
                                    namedEntity = namedEntities.get(candidate);
                                    namedEntity.incrementRepetitions();
                                } else {
                                    // Si la entidad no existe, crea una nueva y añádela al mapa
                                    namedEntity = new NamedEntity(new Category(jsonObject.getString("Category")), jsonObject.getString("label"));
                                    namedEntities.put(candidate, namedEntity);
                                    isNewEntity = true;
                                }
                            
                                if (jsonObject.has("Topics") && isNewEntity) {
                                    JSONArray topics_entity = jsonObject.getJSONArray("Topics");
                                    for (int j = 0; j < topics_entity.length(); j++) {
                                        namedEntity.addTopic(new Topics(topics_entity.getString(j)));
                                    }
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (!found && !namedEntities.containsKey(candidate)) {
                    NamedEntity namedEntity = new NamedEntity(new Category("OTHER"), candidate);
                    namedEntity.addTopic(new Topics("OTHER"));
                    namedEntities.put(candidate, namedEntity);
                }
            }
            // Imprimir las entidades nombradas
            for (NamedEntity namedEntity : namedEntities.values()) {
                namedEntity.namedEntityPrint();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
            // TODO: Print stats
            
            System.out.println("\nStats: ");
            System.out.println("-".repeat(80));
        }

        if (config.getStatsFormat()){
            
            System.out.println("Entro a este if loool");
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


/*
 

// Crear un Set para almacenar los elementos ya vistos
Set<String> vistos = new HashSet<>();
// Eliminar duplicados de la lista candidatos
candidatos.removeIf(e -> !vistos.add(e)); // Si un elemento ya está en el Set, removeIf lo elimina de la lista

// Crear una lista sin duplicados usando stream().distinct().collect()
List<String> candidatosSinDuplicados = candidatos.stream().distinct().collect(Collectors.toList());

// Convertir la lista a un Set para eliminar duplicados
Set<String> candidatosSet = new HashSet<>(candidatos);


 */