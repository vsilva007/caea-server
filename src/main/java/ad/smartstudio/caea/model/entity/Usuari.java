package ad.smartstudio.caea.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuari")
@EqualsAndHashCode(callSuper = false)
public class Usuari extends GEntity {
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	String name;
	@Column
	@EqualsAndHashCode.Exclude
	String surname;
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	String login;
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	String password;
	@Column(name = "activation_code")
	@EqualsAndHashCode.Exclude
	String activationCode;
	@Column
	@EqualsAndHashCode.Exclude
	Integer roleId;
	@Column
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	Boolean active;
	@Column
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	long lastLogin;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuari_id")
	@EqualsAndHashCode.Exclude
	@NotFound(action = NotFoundAction.IGNORE)
	@ToString.Exclude
	Set<ContactEmail> emails;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuari_id")
	@EqualsAndHashCode.Exclude
	@NotFound(action = NotFoundAction.IGNORE)
	@ToString.Exclude
	Set<ContactSms> smss;
	@Column
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	long loginCount;
	@JsonIgnore
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	Date created;
	@JsonIgnore
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	Date updated;
	@Transient
	String token;

	public Usuari() {
		super();
	}

	public Usuari(UUID id) {
		super(id);
	}
}
