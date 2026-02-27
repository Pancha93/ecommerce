package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.Carrito;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Carrito.
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    Optional<Carrito> findByUsuario(Usuario usuario);
    
    Optional<Carrito> findByUsuarioId(Long usuarioId);
    
    void deleteByUsuarioId(Long usuarioId);
}
