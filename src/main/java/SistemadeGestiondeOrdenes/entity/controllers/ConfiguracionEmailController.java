package SistemadeGestiondeOrdenes.entity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionEmail;
import SistemadeGestiondeOrdenes.entity.services.impl.ConfiguracionEmailServiceImpl;

/**
 * Controlador REST para la configuración de SMTP.
 */
@RestController
@RequestMapping("/api/email-config")
public class ConfiguracionEmailController {

    @Autowired
    private ConfiguracionEmailServiceImpl configuracionEmailService;

    @GetMapping
    public ResponseEntity<ConfiguracionEmail> getConfiguration() {
        return configuracionEmailService.getConfiguration()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<ConfiguracionEmail> updateConfiguration(@RequestBody ConfiguracionEmail config) {
        ConfiguracionEmail updated = configuracionEmailService.updateConfiguration(config);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/test")
    public ResponseEntity<String> testConfiguration(@RequestBody ConfiguracionEmail config) {
        try {
            configuracionEmailService.testSMTPConnection(config);
            return ResponseEntity.ok("Conexión SMTP exitosa. La configuración es válida.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al conectar al servidor SMTP: " + e.getMessage());
        }
    }

}
