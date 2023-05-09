package ATechnics.app.repositories;

import ATechnics.app.models.AirbusTask;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface AirbusRepository extends JpaRepository<AirbusTask, String> {
}