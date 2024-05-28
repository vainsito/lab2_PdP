package namedEntities.heuristics;

import java.util.List;

// Creo esta interfaz para que las clases que implementen esta interfaz tengan que implementar el metodo extractCandidates
// Esto me va a permitir modularizar el codigo, y no tener la necedidad de repetir el codigo
// en funcion de la heuristica que se quiera emplear
public interface Heuristic {
    List<String> extractCandidates(String text);
}
