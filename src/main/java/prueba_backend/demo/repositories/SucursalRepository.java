package prueba_backend.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba_backend.demo.models.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
