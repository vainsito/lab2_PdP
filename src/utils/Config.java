package utils;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private boolean statsFormat = false;
    private String feedKey;
    // Agrego aca el atributo de heuristica para inicializar la config
    private String heuristicConfig;
    private String statSelected;

    public Config(boolean printFeed, boolean computeNamedEntities, boolean statsFormat, String feedKey, String heuristicConfig, String statSelected) {
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.statsFormat = statsFormat;
        this.feedKey = feedKey;
        this.heuristicConfig = heuristicConfig; //Con esto le asignamos al campo privado de la heuristica
        this.statSelected = statSelected;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public boolean getStatsFormat() {
        return statsFormat;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public String getHeuristicConfig() {
        return heuristicConfig; //Creamos un Public String de config 
    }

    public String getStatSelected() {
        return statSelected;
    }
}
