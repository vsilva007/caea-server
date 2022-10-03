package ad.smartstudio.caea.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService extends GenericService {
	private static final Log LOG = LogFactory.getLog("AUDIT");

	public void audit(String user, String action) {
		LOG.debug("User: " + user + " did " + action);
	}

	@Override
	@Deprecated
	public void setDao(JpaRepository dao) {
	}
}
