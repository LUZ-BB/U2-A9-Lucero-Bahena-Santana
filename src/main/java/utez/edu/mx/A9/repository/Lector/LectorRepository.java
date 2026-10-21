package utez.edu.mx.A9.repository.lector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.A9.model.lector.Lector;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
}