package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

@Data
@Entity
@Table(name = "activitat")
@EqualsAndHashCode(callSuper = false)
public class Activitat extends GEntity {
	@Column
	String classification;
	@Column(nullable = false)
	String description;
	@Column(nullable = false)
	Integer quantity;
	@Column(name = "last_updated")
	Long lastUpdated;
	@Column
	Boolean status;
	@Column(name = "grup_id")
	UUID grupId;
	@Transient
	Boolean notification;
}
