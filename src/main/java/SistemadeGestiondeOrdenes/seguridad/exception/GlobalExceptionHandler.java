package SistemadeGestiondeOrdenes.seguridad.exception;

import SistemadeGestiondeOrdenes.seguridad.Interceptor.SecurityContextPersonalizado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import SistemadeGestiondeOrdenes.seguridad.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private SecurityContextPersonalizado securityContextPersonalizado;

    // Manejo de ObjectNotFoundException
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDetails(ex.getMessage()));
    }

    // Manejo de IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(ex.getMessage()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex, HttpServletRequest request) {
        ApiError error = new ApiError();
        error.setHttpCode(ex.getStatus().value());
        boolean nonAdmin = securityContextPersonalizado.isTieneRolNoAdministrador();
        error.setMessage(nonAdmin ? "Ocurrió un error" : ex.getMessage());
        if (!nonAdmin) {
            error.setBackedMessage(ex.getResponseBody());
        }
        error.setUrl(request.getRequestURI());
        error.setMethod(request.getMethod());
        error.setTime(LocalDateTime.now());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    // Manejo genérico para otras excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex) {
        String msg = "Ocurrió un error interno";
        if (!securityContextPersonalizado.isTieneRolNoAdministrador()) {
            msg = ex.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDetails(msg));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDetails> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorDetails("El archivo excede el tamaño máximo permitido."));
    }

    static class ErrorDetails {
        private String message;

        public ErrorDetails(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}