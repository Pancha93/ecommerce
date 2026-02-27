package SistemadeGestiondeOrdenes.entity.repositories;

import jakarta.persistence.Column;
import java.util.List;
import java.util.Optional;
import SistemadeGestiondeOrdenes.entity.entities.Ordenlaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Ordenlaboratorio.
 * Proporciona operaciones CRUD básicas y métodos personalizados de búsqueda
 * basados en los atributos y relaciones de la entidad.
 */
@Repository
public interface OrdenlaboratorioRepository extends JpaRepository<Ordenlaboratorio, Long> {

}
