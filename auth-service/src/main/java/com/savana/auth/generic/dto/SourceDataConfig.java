package com.savana.auth.generic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceDataConfig {
    private String sourceType; // Type de source (CSV, API REST, SQL, etc)
    private String sourceUrl; // URL pour les fichiers CSV, API REST
    private String host; // Hôte de la base de données SQL
    private String database; // Nom de la base de données SQL
    private String tableName; // Nom de la table SQL
    private List<String> columns; // Colonnes à récupérer
    private Map<String, String> columnTypes; // Liste colonnes - types des colonnes
}
