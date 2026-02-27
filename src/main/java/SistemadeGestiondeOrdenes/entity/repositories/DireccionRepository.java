package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.Direccion;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Direccion.
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    
    List<Direccion> findByUsuario(Usuario usuario);
    
    List<Direccion> findByUsuarioId(Long usuarioId);
    
    Optional<Direccion> findByUsuarioIdAndEsPredeterminadaTrue(Long usuarioId);
    
    void deleteByUsuarioId(Long usuarioId);
}
