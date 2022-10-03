package ad.smartstudio.caea.service;

import ad.smartstudio.caea.dao.ActivityDao;
import ad.smartstudio.caea.dao.GroupDao;
import ad.smartstudio.caea.dao.SectionDao;
import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.Grup;
import ad.smartstudio.caea.model.entity.Seccio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService extends GenericService<Activitat, ActivityDao> {
	private GroupDao groupDao;
	private SectionDao sectionDao;

	@Transactional
	public List<Activitat> findAll() {
		return dao.findAll();
	}

	@Transactional
	public List<Seccio> findAllBySections() {
		return sectionDao.findAll(Sort.by("classification").ascending());
	}

	@Transactional
	public Seccio saveSeccio(Seccio s) {
		return this.sectionDao.save(s);
	}

	@Transactional
	public List<Activitat> findActivitatsByGroup(UUID groupId) {
		return new ArrayList<>(groupDao.findById(groupId).orElse(null).getActivitats());
	}

	@Transactional
	public List<Activitat> findActivitatByLastUpdatedIsGreaterThan(Long lastUpdated) {
		return dao.findActivitatByLastUpdatedIsGreaterThan(lastUpdated);
	}

	@Transactional
	public Grup saveGroup(Grup entity) {
		return this.groupDao.save(entity);
	}

	@Override
	@Transactional
	public Activitat save(Activitat entity) {
		entity.setLastUpdated(new Date().getTime());
		return super.save(entity);
	}

	@Override
	@Autowired
	public void setDao(ActivityDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setDao(GroupDao dao) {
		this.groupDao = dao;
	}

	@Autowired
	public void setSectionDao(SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}
}
