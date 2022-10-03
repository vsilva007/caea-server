package ad.smartstudio.caea;

import ad.smartstudio.caea.model.dto.CaeaTableLine;
import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.EmailNotification;
import ad.smartstudio.caea.service.ActivityService;
import ad.smartstudio.caea.service.NotificationService;
import com.sendgrid.*;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class SendNotificationsJob {
	private static final Log LOG = LogFactory.getLog(SendNotificationsJob.class);
	private static final Long CRON_MILIS_DELAY = 900000L;
	private final SendGrid sendGrid;
	private final NotificationService service;
	private final ActivityService activityService;

	@Autowired
	public SendNotificationsJob(SendGrid sendGrid, NotificationService service, ActivityService activityService) {
		this.sendGrid = sendGrid;
		this.service = service;
		this.activityService = activityService;
	}

	@Scheduled(fixedRate = 900000L)
	public void run() {
		// TODO add sms part
		Long lastExecute = new Date().getTime() - CRON_MILIS_DELAY;
		List<Activitat> activitats = this.activityService.findActivitatByLastUpdatedIsGreaterThan(lastExecute);
		List<EmailNotification> emailNotifications = this.service.findEmailNotificationsByActivitatIn(activitats);
		Map<String, List<EmailNotification>> groupedActivities = new HashMap<>();
		emailNotifications.forEach(emailNotification -> {
			if (!groupedActivities.containsKey(emailNotification.getContactEmail().getEmail())) {
				groupedActivities.put(emailNotification.getContactEmail().getEmail(), new ArrayList<>());
			}
			groupedActivities.get(emailNotification.getContactEmail().getEmail()).add(emailNotification);
		});
		for (String email : groupedActivities.keySet()) {
			this.sendMail(email, groupedActivities.get(email));
		}
	}

	private Response sendMail(String email, List<EmailNotification> notifications) {
		Email from = new Email("admin@smartstud.io");
		Email to = new Email(email);
		String subject = "CAEA Classificació d'Activitats Econòmiques Andorrana";
		try {
			Content emailContent = createEmailContent(notifications);
			Mail mail = new Mail(from, subject, to, emailContent);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			return sendGrid.api(request);
		} catch (Exception ex) {
			LOG.debug(ex);
			return null;
		}
	}

	private Content createEmailContent(List<EmailNotification> notifications) throws IOException, URISyntaxException {
		StringBuilder tableRows = new StringBuilder();
		for (EmailNotification notif : notifications) {
			tableRows.append(new CaeaTableLine(notif.getActivitat()).toString());
		}
		String htmlString = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("plantilla_email.html").toURI())));
		htmlString = htmlString.replace("$TABLE", tableRows.toString());
		return new Content("text/html", htmlString);
	}
}
