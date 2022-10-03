package ad.smartstudio.caea.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "seccio")
@EqualsAndHashCode(callSuper = false)
public class Seccio extends GEntity {
	@Column
	String classification;
	@Column(nullable = false)
	String description;
	@Column(nullable = false)
	Integer quantity;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "seccio_id")
	@OrderBy("classification")
	Set<Grup> grups;
}
