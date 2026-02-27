package SistemadeGestiondeOrdenes.entity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionEmail;

public interface ConfiguracionEmailRepository extends JpaRepository<ConfiguracionEmail, Long> {
}
