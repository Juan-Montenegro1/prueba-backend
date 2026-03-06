package prueba_backend.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba_backend.demo.models.Franquicia;

public interface FranquisiaRepository extends JpaRepository<Franquicia, Long> {
}
