package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.Orden;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Orden.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    
    Optional<Orden> findByNumeroOrden(String numeroOrden);
    
    List<Orden> findByUsuario(Usuario usuario);
    
    List<Orden> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    
    List<Orden> findByEstado(Orden.EstadoOrden estado);
    
    List<Orden> findByUsuarioIdAndEstado(Long usuarioId, Orden.EstadoOrden estado);
    
    Long countByUsuarioId(Long usuarioId);
}
