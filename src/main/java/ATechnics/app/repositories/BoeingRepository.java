package ATechnics.app.repositories;

import ATechnics.app.models.BoeingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoeingRepository extends JpaRepository<BoeingTask, String> {
}