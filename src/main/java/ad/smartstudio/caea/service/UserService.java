package ad.smartstudio.caea.service;

import ad.smartstudio.caea.dao.UserDao;
import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.util.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService extends GenericService<Usuari, UserDao> {
	@Transactional
	public Usuari login(Usuari usuari) {
		return dao.findUsuariByLoginAndPassword(usuari.getLogin().toLowerCase(), EncryptionUtils.hash(usuari.getPassword()));
	}

	@Transactional
	public Usuari findByLogin(String login) {
		return dao.findUsuariByLogin(login);
	}

	@Override
	@Autowired
	public void setDao(UserDao dao) {
		this.dao = dao;
	}

	public Usuari findByActivationCode(String code) {
		return dao.findUsuariByActivationCode(code);
	}
}
