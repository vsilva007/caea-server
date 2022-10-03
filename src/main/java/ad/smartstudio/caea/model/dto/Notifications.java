package ad.smartstudio.caea.model.dto;

import ad.smartstudio.caea.model.entity.EmailNotification;
import ad.smartstudio.caea.model.entity.SmsNotification;

import java.util.List;

public class Notifications {
	List<EmailNotification> emails;
	List<SmsNotification> smss;
}
