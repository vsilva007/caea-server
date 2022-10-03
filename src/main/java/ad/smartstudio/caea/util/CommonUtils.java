package ad.smartstudio.caea.util;

import java.util.UUID;

public class CommonUtils {

	public static UUID parseId(String id) {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			return null;
		}
	}
}
