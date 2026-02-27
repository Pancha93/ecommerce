package SistemadeGestiondeOrdenes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableAsync
@EntityScan(basePackages = "SistemadeGestiondeOrdenes.entity.entities")
public class DataEntryApplication {
    private static final Logger log = LoggerFactory.getLogger(DataEntryApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DataEntryApplication.class, args);
        try {
            log.info("El servicio del backend esta funcionando en el puerto 8080");
        } catch (Exception e) {
            log.error("Error en el proceso: {}", e.getMessage(), e);
        }
    }
}
