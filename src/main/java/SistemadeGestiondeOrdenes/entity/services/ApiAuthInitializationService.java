package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;
import SistemadeGestiondeOrdenes.entity.repositories.ConfiguracionApiRepository;
import SistemadeGestiondeOrdenes.entity.repositories.ConfiguracionApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servicio que maneja la inicialización automática de autenticación para APIs externas.
 * Se ejecuta al iniciar la aplicación y obtiene tokens para todas las APIs configuradas.
 */
@Service
public class ApiAuthInitializationService implements CommandLineRunner {

    @Autowired
    private ConfiguracionApiRepository configuracionApiRepository;

    // Mapa para almacenar tokens por URL base (simula localStorage del backend)
    private static final Map<String, String> tokenStorage = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando autenticación automática para APIs externas...");

        // Obtener todas las configuraciones de API activas
        List<ConfiguracionApi> configuraciones = configuracionApiRepository.findByActivoTrue();

        for (ConfiguracionApi config : configuraciones) {
            try {
                // Solo procesar APIs que necesiten autenticación
                if (config.getEndpointLogin() != null && !config.getEndpointLogin().isEmpty()) {
                    System.out.println("Iniciando sesión para API: " + config.getUrlBase());

                    String token = obtenerTokenAutenticacion(config);
                    if (token != null) {
                        tokenStorage.put(config.getUrlBase(), token);
                        System.out.println("Token obtenido exitosamente para: " + config.getUrlBase());
                    } else {
                        System.out.println("No se pudo obtener token para: " + config.getUrlBase());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al autenticar API " + config.getUrlBase() + ": " + e.getMessage());
            }
        }

      }

    /**
     * Obtiene el token de autenticación para una configuración de API específica.
     */
    private String obtenerTokenAutenticacion(ConfiguracionApi config) {
        try {
            String urlLogin = config.getUrlBase() + config.getEndpointLogin();
            System.out.println("Conectando a: " + urlLogin);

            // Crear objeto de credenciales
            ObjectNode credenciales = objectMapper.createObjectNode();
            credenciales.put(config.getCampoUsuario(), config.getUsuario());
            credenciales.put("password", config.getPassword());

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlLogin))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(credenciales)))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode responseNode = objectMapper.readTree(response.body());
                String token = extraerTokenDeRespuesta(responseNode);
                if (token != null) {
                    return token;
                }
            }

            System.err.println("Error en login para " + config.getUrlBase() + ": " + response.statusCode() + " - " + response.body());
            return null;

        } catch (Exception e) {
            System.err.println("Error al obtener token para " + config.getUrlBase() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrae el token de la respuesta de la API.
     */
    private String extraerTokenDeRespuesta(JsonNode response) {
        // Buscar token en diferentes ubicaciones posibles
        String[] posiblesNombres = {"token", "accessToken", "access_token", "authToken", "jwt", "bearer"};

        // Buscar en el nivel raíz
        for (String nombre : posiblesNombres) {
            if (response.has(nombre) && !response.get(nombre).isNull()) {
                return response.get(nombre).asText();
            }
        }

        // Buscar en propiedades comunes anidadas
        String[] propiedadesComunes = {"data", "response", "result", "body", "content"};
        for (String prop : propiedadesComunes) {
            if (response.has(prop) && response.get(prop).isObject()) {
                JsonNode nodoAnidado = response.get(prop);
                for (String nombre : posiblesNombres) {
                    if (nodoAnidado.has(nombre) && !nodoAnidado.get(nombre).isNull()) {
                        return nodoAnidado.get(nombre).asText();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Obtiene el token almacenado para una URL base específica.
     */
    public static String getToken(String urlBase) {
        return tokenStorage.get(urlBase);
    }

    /**
     * Almacena un token para una URL base específica.
     */
    public static void setToken(String urlBase, String token) {
        tokenStorage.put(urlBase, token);
    }

    /**
     * Verifica si existe un token para una URL base específica.
     */
    public static boolean hasToken(String urlBase) {
        return tokenStorage.containsKey(urlBase) && tokenStorage.get(urlBase) != null;
    }

    /**
     * Elimina el token para una URL base específica.
     */
    public static void removeToken(String urlBase) {
        tokenStorage.remove(urlBase);
    }

    /**
     * Renueva el token para una URL base específica.
     */
    public static void renewToken(String urlBase, String newToken) {
        tokenStorage.put(urlBase, newToken);
    }

    /**
     * Verifica si las credenciales han cambiado entre dos configuraciones
     */
    public boolean hanCambiadoCredenciales(ConfiguracionApi vieja, ConfiguracionApi nueva) {
        return !Objects.equals(vieja.getUsuario(), nueva.getUsuario()) ||
               !Objects.equals(vieja.getPassword(), nueva.getPassword()) ||
               !Objects.equals(vieja.getCampoUsuario(), nueva.getCampoUsuario()) ||
               !Objects.equals(vieja.getEndpointLogin(), nueva.getEndpointLogin());
    }

}
