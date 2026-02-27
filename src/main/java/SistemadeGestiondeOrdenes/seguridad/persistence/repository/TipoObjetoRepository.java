package SistemadeGestiondeOrdenes.seguridad.persistence.repository;

import SistemadeGestiondeOrdenes.seguridad.persistence.entities.TipoObjeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoObjetoRepository extends JpaRepository<TipoObjeto, Long> {

    Optional<TipoObjeto> findByDescripcion(String descripcion);
}
