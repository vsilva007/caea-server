package ad.smartstudio.caea.security.utils;

import ad.smartstudio.caea.util.EncryptionUtils;

public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {
	private static PasswordEncoder INSTANCE;

	@Override
	public String encode(CharSequence charSequence) {
		return EncryptionUtils.hash(charSequence.toString());
	}

	@Override
	public boolean matches(CharSequence charSequence, String s) {
		return encode(charSequence).equals(s);
	}

	public static PasswordEncoder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PasswordEncoder();
		}
		return INSTANCE;
	}

	public PasswordEncoder() {
	}
}
