package ad.smartstudio.caea.controller;

import ad.smartstudio.caea.aspect.Audit;
import ad.smartstudio.caea.model.entity.*;
import ad.smartstudio.caea.service.ActivityService;
import ad.smartstudio.caea.service.NotificationService;
import error.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static ad.smartstudio.caea.util.CommonUtils.parseId;

@RestController
@RequestMapping(path = "/v1")
public class NotificationController {
	private NotificationService service;
	private ActivityService activityService;

	@Autowired
	public NotificationController(NotificationService service, ActivityService activityService) {
		this.service = service;
		this.activityService = activityService;
	}

	@Audit(message = "Notifications")
	@GetMapping(value = { "/notify/{userId}", "/notify/{userId}/{groupId}", "/notify/{userId}/{groupId}/{activityId}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> notify(@PathVariable(name = "userId") String userId, @PathVariable(name = "groupId", required = false) String groupId, @PathVariable(name = "activityId", required = false) String activityId) {
		UUID userUuid = parseId(userId);
		UUID groupUuid = parseId(groupId);
		UUID activityUuid = parseId(activityId);
		List<ContactEmail> contactEmails = this.service.findContactEmailsByUsuariId(userUuid);
		List<ContactSms> contactSmss = this.service.findContactSmssByUsuariId(userUuid);
		List<Activitat> activitats = new ArrayList<>();
		if (null == groupUuid && null == activityUuid) {
			activitats = this.activityService.findAll();
		} else if (null == activityUuid) {
			activitats = this.activityService.findActivitatsByGroup(groupUuid);
		} else if (null == groupUuid) {
			activitats.add(this.activityService.findById(activityUuid));
		}
		List<EmailNotification> emailNotifications = new ArrayList<>();
		List<SmsNotification> smsNotifications = new ArrayList<>();
		for (Activitat act : activitats) {
			for (ContactEmail contactEmail : contactEmails) {
				EmailNotification emailNotification = new EmailNotification();
				emailNotification.setActivitat(act);
				emailNotification.setContactEmail(contactEmail);
				emailNotifications.add(emailNotification);
			}
			for (ContactSms contactSms : contactSmss) {
				SmsNotification smsNotification = new SmsNotification();
				smsNotification.setActivitat(act);
				smsNotification.setContactSms(contactSms);
				smsNotifications.add(smsNotification);
			}
		}
		this.service.saveAllEmailNotifications(emailNotifications);
		this.service.saveAllSmsNotifications(smsNotifications);
		return ResponseEntity.ok("Done");
	}

	@Audit(message = "Notifications")
	@GetMapping(value = { "/unnotify/{userId}", "/unnotify/{userId}/{groupId}", "/unnotify/{userId}/{groupId}/{activityId}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> unnotify(@PathVariable(name = "userId") String userId, @PathVariable(name = "groupId", required = false) String groupId, @PathVariable(name = "activityId", required = false) String activityId) {
		UUID userUuid = parseId(userId);
		UUID groupUuid = parseId(groupId);
		UUID activityUuid = parseId(activityId);
		List<ContactEmail> contactEmails = this.service.findContactEmailsByUsuariId(userUuid);
		List<ContactSms> contactSmss = this.service.findContactSmssByUsuariId(userUuid);
		List<EmailNotification> emailNotifications = this.service.findEmailNotificationsByContactIn(contactEmails);
		List<SmsNotification> smsNotifications = this.service.findSmsNotificationsByContactIn(contactSmss);
		List<EmailNotification> filteredEmailNotifications = new ArrayList<>();
		List<SmsNotification> filteredSmsNotifications = new ArrayList<>();
		if (null == activityUuid && null != groupId) {
			emailNotifications.forEach(notif -> {
				if (notif.getActivitat().getGrupId().equals(groupUuid)){
					filteredEmailNotifications.add(notif);
				}
			});
			smsNotifications.forEach(notif -> {
				if (notif.getActivitat().getGrupId().equals(groupUuid)){
					filteredSmsNotifications.add(notif);
				}
			});
		} else if (null == groupUuid && null != activityId) {
			emailNotifications.forEach(notif -> {
				if (notif.getActivitat().getId().equals(activityUuid)){
					filteredEmailNotifications.add(notif);
				}
			});
			smsNotifications.forEach(notif -> {
				if (notif.getActivitat().getId().equals(activityUuid)){
					filteredSmsNotifications.add(notif);
				}
			});
		}
		this.service.deleteAllEmailNotifications(filteredEmailNotifications);
		this.service.deleteAllSmsNotifications(filteredSmsNotifications);
		return ResponseEntity.ok("Done");
	}

	@Audit(message = "Notifications")
	@GetMapping(value = { "/notifications/{userId}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<UUID>> getActivitiesToBeNotifiedForUser(@PathVariable(name = "userId") String userId) {
		UUID userUuid = parseId(userId);
		List<ContactEmail> contactEmails = this.service.findContactEmailsByUsuariId(userUuid);
		List<ContactSms> contactSmss = this.service.findContactSmssByUsuariId(userUuid);
		List<EmailNotification> emailNotifications = this.service.findEmailNotificationsByContactIn(contactEmails);
		List<SmsNotification> smsNotifications = this.service.findSmsNotificationsByContactIn(contactSmss);
		Set<UUID> notificationActivities = new HashSet<>();
		for (EmailNotification emailNotification : emailNotifications) {
			notificationActivities.add(emailNotification.getActivitat().getId());
		}
		for (SmsNotification smsNotification : smsNotifications) {
			notificationActivities.add(smsNotification.getActivitat().getId());
		}
		return ResponseEntity.ok(notificationActivities);
	}
}
