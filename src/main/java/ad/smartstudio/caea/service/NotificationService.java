package ad.smartstudio.caea.service;

import ad.smartstudio.caea.dao.ContactEmailDao;
import ad.smartstudio.caea.dao.ContactSmsDao;
import ad.smartstudio.caea.dao.EmailNotificationDao;
import ad.smartstudio.caea.dao.SmsNotificationDao;
import ad.smartstudio.caea.model.entity.*;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class NotificationService extends GenericService<GEntity, JpaRepository<GEntity, UUID>> {
	private static final Log LOG = LogFactory.getLog(NotificationService.class);
	ContactEmailDao contactEmailDao;
	ContactSmsDao contactSmsDao;
	SmsNotificationDao smsNotificationDao;
	EmailNotificationDao emailNotificationDao;

	@Transactional
	public List<ContactEmail> findContactEmailsByUsuariId(UUID usuariId) {
		return this.contactEmailDao.findContactEmailsByUsuariId(usuariId);
	}

	@Transactional
	public List<ContactSms> findContactSmssByUsuariId(UUID usuariId) {
		return this.contactSmsDao.findContactSmssByUsuariId(usuariId);
	}

	public List<EmailNotification> saveAllEmailNotifications(List<EmailNotification> emailNotifications) {
		List<EmailNotification> result = new ArrayList<>();
		emailNotifications.forEach(notif -> {
			try {
				result.add(this.saveEmailNotification(notif));
			} catch (DataIntegrityViolationException e) {
				LOG.debug("Duplicate line: " + notif.getContactEmail().getEmail() + " : " + notif.getActivitat().getId());
			}
		});
		return result;
	}

	@Transactional
	public EmailNotification saveEmailNotification(EmailNotification emailNotification) {
		return this.emailNotificationDao.save(emailNotification);
	}

	@Transactional
	public SmsNotification saveSmsNotification(SmsNotification smsNotification) {
		return this.smsNotificationDao.save(smsNotification);
	}

	public List<SmsNotification> saveAllSmsNotifications(List<SmsNotification> smsNotifications) {
		List<SmsNotification> result = new ArrayList<>();
		smsNotifications.forEach(notif -> {
			try {
				result.add(this.saveSmsNotification(notif));
			} catch (DataIntegrityViolationException e) {
				LOG.debug("Duplicate line: " + notif.getContactSms().getPhoneNumber() + " : " + notif.getActivitat().getId());
			}
		});
		return result;
	}

	@Transactional
	public void saveUserContacts(UUID userId, Set<ContactSms> smss, Set<ContactEmail> emails) {
		if (!CollectionUtils.isEmpty(new ArrayList<>(emails))) {
			emails.forEach(email -> {
				email.setUsuariId(userId);
				this.contactEmailDao.save(email);
			});
		}
		if (!CollectionUtils.isEmpty(new ArrayList<>(smss))) {
			smss.forEach(sms -> {
				sms.setUsuariId(userId);
				this.contactSmsDao.save(sms);
			});
		}
	}

	@Transactional
	public List<EmailNotification> findEmailNotificationsByContactIn(List<ContactEmail> contactEmails) {
		return this.emailNotificationDao.findEmailNotificationsByContactEmailIn(contactEmails);
	}

	@Transactional
	public List<SmsNotification> findSmsNotificationsByContactIn(List<ContactSms> contactSmss) {
		return this.smsNotificationDao.findSmsNotificationsByContactSmsIn(contactSmss);
	}

	@Transactional
	public List<EmailNotification> findEmailNotificationsByActivitatIn(List<Activitat> activitats) {
		return this.emailNotificationDao.findEmailNotificationsByActivitatIn(activitats);
	}

	@Transactional
	public List<SmsNotification> findSmsNotificationsByActivitatIn(List<Activitat> activitats) {
		return this.smsNotificationDao.findSmsNotificationsByActivitatIn(activitats);
	}

	@Transactional
	public void deleteAllEmailNotifications(List<EmailNotification> filteredEmailNotifications) {
		this.emailNotificationDao.deleteAll(filteredEmailNotifications);
	}

	@Transactional
	public void deleteAllSmsNotifications(List<SmsNotification> filteredSmsNotifications) {
		this.smsNotificationDao.deleteAll(filteredSmsNotifications);
	}

	@Autowired
	public void setContactEmailDao(ContactEmailDao contactEmailDao) {
		this.contactEmailDao = contactEmailDao;
	}

	@Autowired
	public void setContactSmsDao(ContactSmsDao contactSmsDao) {
		this.contactSmsDao = contactSmsDao;
	}

	@Autowired
	public void setSmsNotificationDao(SmsNotificationDao smsNotificationDao) {
		this.smsNotificationDao = smsNotificationDao;
	}

	@Autowired
	public void setEmailNotificationDao(EmailNotificationDao emailNotificationDao) {
		this.emailNotificationDao = emailNotificationDao;
	}

	@Override
	@Deprecated
	public void setDao(JpaRepository<GEntity, UUID> dao) {
	}
}
