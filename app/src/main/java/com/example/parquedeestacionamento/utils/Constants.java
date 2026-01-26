package com.example.parquedeestacionamento.utils;

/**
 * Constantes centralizadas da aplicação.
 * Facilita manutenção e evita valores hardcoded dispersos no código.
 */
public final class Constants {

    private Constants() {
        // Construtor privado para prevenir instanciação
    }

    // ==================== Credenciais Default ====================

    /** Username do administrador criado na primeira execução */
    public static final String DEFAULT_ADMIN_USERNAME = "admin";

    /** Password do administrador criado na primeira execução */
    public static final String DEFAULT_ADMIN_PASSWORD = "1234";

    // ==================== Animações ====================

    /** Duração padrão de animações em milissegundos */
    public static final long ANIMATION_DURATION_MS = 500L;

    // ==================== Mapa ====================

    /** Número máximo de marcadores a mostrar no mapa */
    public static final int MAP_MAX_MARKERS = 60;

    /** Raio de busca de parques em metros */
    public static final int MAP_SEARCH_RADIUS_METERS = 10000;

    /** Zoom padrão do mapa */
    public static final float MAP_DEFAULT_ZOOM = 15f;

    /** Latitude padrão (Beja, Portugal) */
    public static final double MAP_DEFAULT_LATITUDE = 38.01577;

    /** Longitude padrão (Beja, Portugal) */
    public static final double MAP_DEFAULT_LONGITUDE = -7.875039;

    // ==================== Formatos ====================

    /** Formato de data/hora para exibição */
    public static final String DATE_TIME_FORMAT = "dd/MM HH:mm";

    // ==================== Validação ====================

    /**
     * Regex para validar matrículas portuguesas.
     * Formatos aceites: XX-XX-XX (letras e números)
     */
    public static final String PLATE_REGEX = "^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}$";
}
