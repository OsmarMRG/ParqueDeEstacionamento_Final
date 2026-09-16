package com.example.parquedeestacionamento.utils;

// Constantes usadas em toda a aplicação
public final class Constants {

    private Constants() {}

    // Credenciais do utilizador admin criado na primeira execução
    public static final String DEFAULT_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "1234";

    // Configurações do mapa
    public static final int MAP_MAX_MARKERS = 60;
    public static final int MAP_SEARCH_RADIUS_METERS = 10000;
    public static final float MAP_DEFAULT_ZOOM = 15f;
    public static final double MAP_DEFAULT_LATITUDE = 38.01577;   // Beja
    public static final double MAP_DEFAULT_LONGITUDE = -7.875039;

    // Formato de data para mostrar entradas e saídas
    public static final String DATE_TIME_FORMAT = "dd/MM HH:mm";

    // Regex para validar matrículas portuguesas (ex: 00-AA-00)
    public static final String PLATE_REGEX = "^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}$";
}
