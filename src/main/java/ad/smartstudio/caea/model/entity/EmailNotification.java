package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "email_notification")
@EqualsAndHashCode(callSuper = false)
public class EmailNotification extends GEntity {
	@JsonIgnore
	@ManyToOne(targetEntity = ContactEmail.class)
	ContactEmail contactEmail;
	@JsonIgnore
	@ManyToOne(targetEntity = Activitat.class)
	Activitat activitat;
}
