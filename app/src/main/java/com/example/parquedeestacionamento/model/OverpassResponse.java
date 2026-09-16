package com.example.parquedeestacionamento.model;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

// Modelo da resposta da API Overpass (OpenStreetMap)
public class OverpassResponse {

    @Nullable
    public List<Element> elements;

    // Cada elemento é um nó geográfico (parque de estacionamento)
    public static class Element {
        @Nullable
        public String type;
        public long id;
        public double lat;
        public double lon;
        @Nullable
        public Map<String, String> tags;  // tags do OSM (nome, etc.)
    }
}
