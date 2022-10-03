package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.ContactSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactSmsDao extends JpaRepository<ContactSms, UUID> {
	public List<ContactSms> findContactSmssByUsuariId(UUID usuariId);
}
