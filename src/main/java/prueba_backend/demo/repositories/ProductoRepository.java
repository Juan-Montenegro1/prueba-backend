package prueba_backend.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba_backend.demo.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{  
}
