package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "contact_sms")
@EqualsAndHashCode(callSuper = false)
public class ContactSms extends GEntity {
	@Column(name = "usuari_id")
	UUID usuariId;
	@Column
	@EqualsAndHashCode.Exclude
	String phoneNumber;
}
