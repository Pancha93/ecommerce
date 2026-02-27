package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.Producto;
import SistemadeGestiondeOrdenes.entity.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Producto.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findBySku(String sku);
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByDestacadoTrue();
    
    List<Producto> findByCategoria(Categoria categoria);
    
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Producto> buscarProductos(@Param("busqueda") String busqueda);
    
    List<Producto> findByActivoTrueOrderByFechaCreacionDesc();
    
    List<Producto> findByActivoTrueAndPrecioOfertaIsNotNullOrderByPrecioOfertaAsc();
}
