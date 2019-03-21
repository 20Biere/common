package be.itlive.common.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Environment Enum.
 * 
 * @author vbiertho
 */
public enum Env {

	/**
	 * DEV.
	 */
	DEV("dev", "Developement"),
	/**
	 * TEST.
	 */
	TEST("test", "Test"),
	/**
	 * ACC.
	 */
	ACC("acc", "Acceptance"),
	/**
	 * PRODUCTION.
	 */
	PROD("prod", "Production");

	/**
	 * Short Name.
	 */
	private String shortName;

	/**
	 * Long Name.
	 */
	private String longName;

	private static final Map<String, Env> LOOKUP = new HashMap<>();

	static {
		for (Env s : EnumSet.allOf(Env.class)) {
			LOOKUP.put(s.getShortName(), s);
		}
	}

	Env(final String shortName, final String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @return the longName
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * @param name target.
	 * @return the Env.
	 */
	public static Env get(final String name) {
		return LOOKUP.get(StringUtils.lowerCase(name));
	}

	/**
	 * @param environment to check.
	 * @return true if Dev.
	 */
	public static boolean isDev(final String environment) {
		return is(environment, Env.DEV);
	}

	/**
	 * @param environment to check.
	 * @return true if Test.
	 */
	public static boolean isTest(final String environment) {
		return is(environment, Env.TEST);
	}

	/**
	 * @param environment to check.
	 * @return true if Validation.
	 */
	public static boolean isVal(final String environment) {
		return is(environment, Env.ACC);
	}

	/**
	 * @param environment to check.
	 * @return true if Production.
	 */
	public static boolean isProd(final String environment) {
		return is(environment, Env.PROD);
	}

	/**
	 * @param environmentString to check.
	 * @param match             Environment match
	 * @return true if match.
	 */
	public static boolean is(final String environmentString, final Env match) {
		Env env = get(environmentString);
		return match.equals(env);
	}
}
