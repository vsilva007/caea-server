package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.ContactEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactEmailDao extends JpaRepository<ContactEmail, UUID> {
	public List<ContactEmail> findContactEmailsByUsuariId(UUID usuariId);
}
