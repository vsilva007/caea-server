package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Grup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupDao extends JpaRepository<Grup, UUID> {

}
