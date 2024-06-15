package namedEntities.heuristics;

public class HeuristicMaker {
    public static Heuristic makeHeuristic(String heuristicName) {
        switch (heuristicName) {
            case "acronym":
                return new AcronymWordHeuristic();
            case "preceded":
                return new PrecededWordHeuristic();
            case "capitalized":
                return new CapitalizedWordHeuristic();
            default:
                throw new IllegalArgumentException("Error!: Heuristic not found, please check the heuristic name and try again.");
        }
    }
}
