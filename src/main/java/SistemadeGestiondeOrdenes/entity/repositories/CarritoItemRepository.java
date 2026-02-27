package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad CarritoItem.
 */
@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    
    List<CarritoItem> findByCarritoId(Long carritoId);
    
    Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
    
    void deleteByCarritoId(Long carritoId);
    
    void deleteByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
