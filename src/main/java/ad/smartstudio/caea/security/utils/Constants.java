package ad.smartstudio.caea.security.utils;

public class Constants {
	// Spring Security
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String HEADER_REFRESH_KEY = "RefreshToken";
	public static final String HEADER_ACCESS_CONTROL_KEY = "Access-Control-Expose-Headers";
	public static final String TOKEN_BEARER_PREFIX = "Bearer";
	// JWT
	public static final String ISSUER_INFO = "CAEA_AD";
	public static final String SUPER_SECRET_KEY = "1%&Wkfa982yfZ=Mlkh";
	public static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24h
	// RefreshToken
	public static final String SUPER_SECRET_KEY_REFRESH = "3%LWCHa-eyfZ=MZEh";
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 864_000_000; // 10 day
}