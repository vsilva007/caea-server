package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sms_notification")
@EqualsAndHashCode(callSuper = false)
public class SmsNotification extends GEntity {
	@JsonIgnore
	@ManyToOne(targetEntity = ContactSms.class)
	ContactSms contactSms;
	@JsonIgnore
	@ManyToOne(targetEntity = Activitat.class)
	Activitat activitat;
}
