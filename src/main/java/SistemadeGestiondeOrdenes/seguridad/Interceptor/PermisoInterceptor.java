package SistemadeGestiondeOrdenes.seguridad.Interceptor;
import SistemadeGestiondeOrdenes.seguridad.service.impl.AuthenticationService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PermisoInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SecurityContextPersonalizado securityContextPersonalizado;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/") || requestURI.startsWith("/api/generacion")
                || requestURI.equals("/proyectos") || requestURI.equals("/proyectos/eliminarProyecto")
                || requestURI.equals("/notificar") || requestURI.equals("/authGitHub/github")) {
            return true;
        }

        // Permitir endpoints públicos del ecommerce
        if (requestURI.equals("/api/categorias/activas") 
                || requestURI.equals("/api/productos/activos")
                || requestURI.equals("/api/productos/destacados")
                || requestURI.equals("/api/productos/nuevos")
                || requestURI.equals("/api/productos/ofertas")
                || requestURI.startsWith("/api/productos/categoria/")
                || requestURI.startsWith("/api/productos/buscar")
                || requestURI.matches("^/api/productos/[0-9]+$")) {
            return true;
        }

        if (requestURI.matches("/api/[^/]+/download") || requestURI.matches("^/api/[^/]+/[^/]+/files$")) {
            return true; // Permitir sin validaciones
        }


        if (requestURI.matches("^/api/[^/]+/notificar$")) {
            return true; // Permitir sin validaciones
        }


        if (requestURI.matches("^/api/email-config$") || requestURI.matches("^/api/email-config/[^/]+$")) {
            return true; // Permitir sin validaciones
        }

        // Endpoint de menú - requiere autenticación pero no headers Accion/Objeto
        if (requestURI.equals("/api/menu/opciones")) {
            String token = jwtTokenService.extractJwtFromRequest(request);
            if (token == null || token.isEmpty()) {
                return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "No se envió el token correctamente.");
            }
            try {
                Date expiration = jwtTokenService.obtenerExpiration(token);
                if (expiration.before(new Date())) {
                    return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "El token ha expirado.");
                }
                return true;
            } catch (Exception e) {
                return enviarError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al validar token: " + e.getMessage());
            }
        }

        // Endpoints protegidos del ecommerce - requieren autenticación pero no headers Accion/Objeto
        if (requestURI.startsWith("/api/carrito") 
                || requestURI.startsWith("/api/ordenes") 
                || requestURI.startsWith("/api/direcciones")) {
            String token = jwtTokenService.extractJwtFromRequest(request);
            if (token == null || token.isEmpty()) {
                return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "No se envió el token correctamente.");
            }
            try {
                Date expiration = jwtTokenService.obtenerExpiration(token);
                if (expiration.before(new Date())) {
                    return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "El token ha expirado.");
                }
                return true;
            } catch (Exception e) {
                return enviarError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al validar token: " + e.getMessage());
            }
        }

        // Configurar la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getHeader("Accion");
        String objeto = request.getHeader("Objeto");

        if (accion == null || accion.isEmpty()) {
            return enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "No se envió la acción.");
        }

        if (objeto == null || objeto.isEmpty()) {
            return enviarError(response, HttpServletResponse.SC_BAD_REQUEST, "No se envió el objeto.");
        }

        String token = jwtTokenService.extractJwtFromRequest(request);
        if (token == null || token.isEmpty()) {
            return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "No se envió el token correctamente.");
        }

        try {
            Date expiration = jwtTokenService.obtenerExpiration(token);
            if (expiration.before(new Date())) {
                return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "El token ha expirado.");
            }

            String userName = jwtTokenService.obtenerUsername(token);
            boolean tieneRolNoAdministrador = (boolean) jwtTokenService.extractAllClaims(token).get("tieneRolNoAdministrador");

            // 🔥 Guardar en el contexto de seguridad
            securityContextPersonalizado.setUsuarioActual(userName);
            securityContextPersonalizado.setTieneRolNoAdministrador(tieneRolNoAdministrador);

            boolean tienePermiso = authenticationService.verificarPermiso(userName, jwtTokenService.obtenerRoles(token), accion, objeto);
            if (!tienePermiso) {
                return enviarError(response, HttpServletResponse.SC_UNAUTHORIZED, "No tiene permisos.");
            }

            return true;

        } catch (Exception e) {
            return enviarError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno: " + e.getMessage());
        }
    }

    private boolean enviarError(HttpServletResponse response, int statusCode, String mensaje) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\":\"" + mensaje + "\"}");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        securityContextPersonalizado.limpiar();
    }

}