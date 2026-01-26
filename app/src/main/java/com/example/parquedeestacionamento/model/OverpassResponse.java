package com.example.parquedeestacionamento.model;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Modelo de resposta da API Overpass.
 * Contém lista de elementos geográficos encontrados na consulta.
 */
public class OverpassResponse {

    /**
     * Lista de elementos geográficos retornados pela query.
     */
    @Nullable
    public List<Element> elements;

    /**
     * Representa um elemento geográfico individual (nó, via, relação).
     */
    public static class Element {

        /**
         * Tipo do elemento (node, way, relation).
         */
        @Nullable
        public String type;

        /**
         * ID único do elemento no OpenStreetMap.
         */
        public long id;

        /**
         * Latitude do elemento (para nós).
         */
        public double lat;

        /**
         * Longitude do elemento (para nós).
         */
        public double lon;

        /**
         * Tags associadas ao elemento (nome, tipo, etc.).
         */
        @Nullable
        public Map<String, String> tags;
    }
}
