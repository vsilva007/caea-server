package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.ContactSms;
import ad.smartstudio.caea.model.entity.SmsNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SmsNotificationDao extends JpaRepository<SmsNotification, UUID> {
	List<SmsNotification> findSmsNotificationsByActivitatIn(List<Activitat> activitats);

	List<SmsNotification> findSmsNotificationsByContactSmsIn(List<ContactSms> contactSmss);
}
