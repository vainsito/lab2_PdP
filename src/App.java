import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import feed.Article;
import feed.FeedParser;
import namedEntities.heuristics.AcronymWordHeuristic;
import namedEntities.heuristics.PrecededWordHeuristic;
import namedEntities.heuristics.CapitalizedWordHeuristic;
import namedEntities.heuristics.Heuristic;

import utils.Config;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;
import utils.NamedEntitiesUtils;

public class App {

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        run(config, feedsDataArray);
    }

    // TODO: Change the signature of this function if needed
    private static void run(Config config, List<FeedsData> feedsDataArray) {
        // If para imprimir el help
        if (config.getHelp()) {
            printHelp(feedsDataArray);
            return;
        }

        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }

        // Inicializacion de la lista de articulos se puede modularizar esto en
        // article.java
        List<Article> allArticles = new ArrayList<>();
        boolean use_feed = config.getFeedProvided() && config.getFeedKey() != null;
        for (FeedsData feedData : feedsDataArray) {
            if (use_feed && config.getFeedKey().equals(feedData.getLabel())) {
                try {
                    String contenido = FeedParser.fetchFeed(feedData.getUrl());
                    allArticles = FeedParser.parseXML(contenido);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else if (use_feed && !config.getFeedKey().equals(feedData.getLabel())) {
                System.out.println("Feed key is not: " + feedData.getLabel() + " Skipping..");
            } else {
                try {
                    String contenido = FeedParser.fetchFeed(feedData.getUrl());
                    List<Article> articles = FeedParser.parseXML(contenido);
                    allArticles.addAll(articles);
                } catch (Exception e) {
                    System.out.println("Error fetching feed: " + feedData.getLabel());
                    e.printStackTrace();
                }
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
            // If para chequear que heuristica se esta utilizando
            Heuristic heuristic = null;

            if (config.getHeuristicConfig().equals("acronym")) {
                heuristic = new AcronymWordHeuristic();
            } else if (config.getHeuristicConfig().equals("preceded")) {
                heuristic = new PrecededWordHeuristic();
            } else if (config.getHeuristicConfig().equals("capitalized")) {
                heuristic = new CapitalizedWordHeuristic();
            } else {
                System.out.println("Error!: Heuristic not found, please check the heuristic name and try again.");
                System.exit(1);
            }
            NamedEntitiesUtils entities_sorted = new NamedEntitiesUtils();
            entities_sorted.sortEntities(allArticles, heuristic);

            // TODO: Print stats

            System.out.println("\nStats: ");
            System.out.println("-".repeat(80));
            // Obtengo la palabra para ver que stat quiero imprimir
            try {
                entities_sorted.printStats(config.getStatSelected());
            } catch (Exception e) {
                System.out.println("Error!: Stat not found, please check the stat name and try again.");
                System.exit(1);
            }
            // TODO: Print the stats in the specified format, si es cat, imprime por
            // categorias, si es topic imprime por topic

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
        System.out.println("                                       Available heuristic names are:");
        System.out.println("                                           - acronym: Acronym-based heuristic");
        System.out.println("                                           - preceded: Preceded word heuristic");
        System.out.println("                                           - capitalized: Capitalized word heuristic");
        // TODO: Print the available heuristics with the following format
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

}
