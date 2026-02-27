package SistemadeGestiondeOrdenes.seguridad.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PermisoInterceptor permisoInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permisoInterceptor)
                .addPathPatterns("/**") // Aplica a todas las rutas
                .excludePathPatterns(
                        "/auth/crearUsuario",
                        "/auth/authenticate",
                        "/api/upload-import-file",
                        "/api/import-files"
                );
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:4200", "http://localhost:8080", "http://127.0.0.1:4200", "http://127.0.0.1:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Accion", "Objeto", "Content-Type", "Authorization")
                .exposedHeaders("Accion", "Objeto")
                .allowCredentials(true);
    }
}