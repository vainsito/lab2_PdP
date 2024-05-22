package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONFilter {
        static public List<String> parseJsonFilterData(String jsonFilePath, String filter_word) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<String> filterList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String palabra = jsonObject.getString(filter_word);
            // If para chequear que la palabra no esta en la lista
            if(!filterList.contains(palabra)){
                filterList.add(palabra);
            }
        }
        return filterList;
    }
}
