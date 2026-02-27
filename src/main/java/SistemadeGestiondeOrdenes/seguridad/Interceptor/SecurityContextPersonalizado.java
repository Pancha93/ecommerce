package SistemadeGestiondeOrdenes.seguridad.Interceptor;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextPersonalizado {
    private String usuarioActual = "ANONYMOUS"; // Valor por defecto
    private boolean tieneRolNoAdministrador = false;

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean isTieneRolNoAdministrador() {
        return tieneRolNoAdministrador;
    }

    public void setTieneRolNoAdministrador(boolean tieneRolNoAdministrador) {
        this.tieneRolNoAdministrador = tieneRolNoAdministrador;
    }

    public void limpiar() {
        this.usuarioActual = "ANONYMOUS";
        this.tieneRolNoAdministrador = false;
    }
}
