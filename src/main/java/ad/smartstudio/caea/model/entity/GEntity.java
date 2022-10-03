package ad.smartstudio.caea.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class GEntity {
	@Id
	@Column
	protected UUID id;

	public GEntity() {
		if (null == this.id)
			this.id = this.generateId();
	}

	public GEntity(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public UUID generateId() {
		return UUID.randomUUID();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GEntity)) {
			return false;
		}
		GEntity other = (GEntity) obj;
		return this.id.equals(other.getId());
	}
}
