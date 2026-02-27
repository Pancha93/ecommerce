package SistemadeGestiondeOrdenes.entity.repositories;

import SistemadeGestiondeOrdenes.entity.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional<Categoria> findByNombre(String nombre);
    
    List<Categoria> findByActivoTrue();
    
    List<Categoria> findByActivoOrderByNombreAsc(Boolean activo);
}
