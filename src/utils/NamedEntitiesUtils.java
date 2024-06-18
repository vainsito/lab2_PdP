package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.HashSet;

import feed.Article;
import namedEntities.Category;
import namedEntities.NamedEntity;
import namedEntities.Topics;
import namedEntities.heuristics.Heuristic;


// Clase que se encarga de ordenar las entidades y de imprimir las estadistica
public class NamedEntitiesUtils {
    private Map<String, NamedEntity> namedEntities;

    // Set para almacenar las categorias existentes
    private Set<String> categories;

    // Set para almacenar los tópicos existentes
    private Set<String> topics;

    // Constructor
    public NamedEntitiesUtils() {
        this.namedEntities = new HashMap<>();
        this.categories = new HashSet<>();
        this.topics = new HashSet<>();
    }

    // Metodos

    public void sortEntities(JavaRDD<String> lines, String heuristic) {
        heuristic = new makeHeuristic();
        try{
            JavaRDD<String> candidatos = heuristic.extractCandidates(lines, heuristic);
        } catch (IllegalArgumentException e) {
            System.exit(1);
        } 
        
        List<String> candidatosLISTA = candidatos.collect();

        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/dictionary.json")),
                    StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);

            // Mapa para almacenar las entidades nombradas, utilizando namedEntities

            for (String candidate : candidatosLISTA) {
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
                                if (this.namedEntities.containsKey(candidate)) {
                                    // Si la entidad ya existe, incrementa el campo repetitions
                                    namedEntity = this.namedEntities.get(candidate);
                                    namedEntity.incrementRepetitions();
                                } else {
                                    Category category_entity = new Category(jsonObject.getString("Category"));
                                    // Si la entidad no existe, crea una nueva y añádela al mapa
                                    namedEntity = new NamedEntity(category_entity, jsonObject.getString("label"));
                                    this.namedEntities.put(candidate, namedEntity);
                                    // Si la entidad es nueva, añade los tópicos y categorías
                                    this.categories.add(category_entity.getName());
                                    isNewEntity = true;
                                }

                                if (jsonObject.has("Topics") && isNewEntity) {
                                    JSONArray topics_entity = jsonObject.getJSONArray("Topics");
                                    for (int j = 0; j < topics_entity.length(); j++) {
                                        Topics topico = new Topics(topics_entity.getString(j));
                                        namedEntity.addTopic(topico);
                                        // Si el tópico no existe, añádelo a la lista de tópicos
                                        this.topics.add(topico.getName());
                                    }
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (!found && !this.namedEntities.containsKey(candidate)) {
                    NamedEntity namedEntity = new NamedEntity(new Category("OTHER"), candidate);
                    namedEntity.addTopic(new Topics("OTHER"));
                    this.namedEntities.put(candidate, namedEntity);
                    // Puede cambiar lo d abajo
                    this.categories.add("OTHER");
                    this.topics.add("OTHER");
                }
            }
            // Imprimir las entidades nombradas
            for (NamedEntity namedEntity : this.namedEntities.values()) {
                namedEntity.namedEntityPrint();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // metodo para imprimir las entidades nombradas
    public void printNamedEntities() {
        for (NamedEntity namedEntity : this.namedEntities.values()) {
            namedEntity.namedEntityPrint();
        }
    }

    // Metodo para imprimir las estadisticas de repetición de las entidades
    // nombradas

    public void printStats(String statsSelected) {
        // Si statsSelected es "cat" se imprimen las repeticiones de las entidades
        // nombradas por categoría
        // Si statsSelected es "top" se imprimen las repeticiones de las entidades
        // nombradas por tópico

        if (statsSelected.equals("cat")) {
            for (String category : this.categories) {
                System.out.println("Category: " + category);
                for (NamedEntity namedEntity : this.namedEntities.values()) {
                    if (namedEntity.getCategory().getName().equals(category)) {
                        System.out.println(namedEntity.getName() + ": " + namedEntity.getRepetitions());
                    }
                }
            }
        } else if (statsSelected.equals("top")) {
            for (String topic : this.topics) {
                System.out.println("Topic: " + topic);
                for (NamedEntity namedEntity : this.namedEntities.values()) {
                    for (Topics t : namedEntity.getTopics()) {
                        if (t.getName().equals(topic)) {
                            System.out.println(namedEntity.getName() + ": " + namedEntity.getRepetitions());
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid stats option");
        }
    }
}