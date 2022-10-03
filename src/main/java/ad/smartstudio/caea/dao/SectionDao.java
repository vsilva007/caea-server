package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Seccio;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.UUID;

public interface SectionDao extends JpaRepository<Seccio, UUID> {
}
