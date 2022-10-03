package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.ContactEmail;
import ad.smartstudio.caea.model.entity.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailNotificationDao extends JpaRepository<EmailNotification, UUID> {
	List<EmailNotification> findEmailNotificationsByActivitatIn(List<Activitat> activitats);

	List<EmailNotification> findEmailNotificationsByContactEmailIn(List<ContactEmail> contactEmails);
}
