package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "grup")
@EqualsAndHashCode(callSuper = false)
public class Grup extends GEntity {
	@Column
	String classification;
	@Column(nullable = false)
	String description;
	@Column(nullable = false)
	Integer quantity;
	@Column(name = "seccio_id")
	UUID seccioId;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "grup_id")
	@OrderBy("classification")
	Set<Activitat> activitats;
}
