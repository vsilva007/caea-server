package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Activitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityDao extends JpaRepository<Activitat, UUID> {
	List<Activitat> findActivitatByLastUpdatedIsGreaterThan(Long lastUpdated);
}
