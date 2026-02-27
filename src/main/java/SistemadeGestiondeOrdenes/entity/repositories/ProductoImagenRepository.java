package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.ProductoImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad ProductoImagen.
 */
@Repository
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Long> {
    
    List<ProductoImagen> findByProductoIdOrderByOrdenAsc(Long productoId);
    
    Optional<ProductoImagen> findByProductoIdAndEsPrincipalTrue(Long productoId);
    
    void deleteByProductoId(Long productoId);
}
