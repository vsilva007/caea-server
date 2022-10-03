package ad.smartstudio.caea.dao;

import ad.smartstudio.caea.model.entity.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<Usuari, UUID> {
	Usuari findUsuariByLogin(String login);

	Usuari findUsuariByLoginAndPassword(String login, String password);

	Usuari findUsuariByActivationCode(String activationCode);
}
