package SistemadeGestiondeOrdenes.seguridad.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para manejar errores de API.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String responseBody;

    public ApiException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ApiException(HttpStatus status, String message, String responseBody) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }
}