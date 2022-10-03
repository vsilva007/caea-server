package ad.smartstudio.caea.service;

import ad.smartstudio.caea.model.entity.GEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public abstract class GenericService<T extends GEntity, D extends JpaRepository<T, UUID>> {
	protected D dao;

	public T save(T entity) {
		if (null == entity.getId())
			entity.setId(entity.generateId());
		return dao.save(entity);
	}

	public T findById(UUID id) {
		return dao.findById(id).orElse(null);
	}

	public abstract void setDao(D dao);

	public List<T> findAll(){
		return dao.findAll();
	}
}
