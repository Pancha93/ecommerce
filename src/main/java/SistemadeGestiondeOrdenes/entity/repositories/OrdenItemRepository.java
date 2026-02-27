package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.OrdenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad OrdenItem.
 */
@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
    
    List<OrdenItem> findByOrdenId(Long ordenId);
    
    void deleteByOrdenId(Long ordenId);
}
