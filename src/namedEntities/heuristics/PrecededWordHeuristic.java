package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrecededWordHeuristic implements Heuristic{

    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");
        // Checkear el tema del espacio, pq aca no aparece Milei ponele
        Pattern pattern = Pattern.compile("(?:Sr\\.|Sra\\.|Dr\\.|Dra\\.|Lic\\.|Ing\\.|el|El|la|La|los|Los|las|Las|yo|tu|ella|nosotros|vosotros|ellos|ellas)\\s([A-Z][a-z]+(?:\\s[A-Z][a-z]+)?)");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String match = matcher.group(1);
            candidates.add(match);
        }
        return candidates;
    }
}