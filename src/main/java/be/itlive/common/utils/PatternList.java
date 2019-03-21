package be.itlive.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 *
 * @author vbiertho
 *
 */
public class PatternList extends ArrayList<Pattern> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PatternList() {
	}

	public PatternList(final Collection<? extends Pattern> inC) {
		super(inC);
	}

	public PatternList(final String... inS) {
		for (String s : inS) {
			add(s);
		}
	}

	public boolean add(final String inS) {
		return super.add(Pattern.compile(inS));
	}

	public boolean matchesAny(final String s) {
		for (Pattern p : this) {
			if (p.matcher(s).matches()) {
				return true;
			}
		}
		return false;
	}

	public boolean matchesAll(final String s) {
		for (Pattern p : this) {
			if (!p.matcher(s).matches()) {
				return false;
			}
		}
		return true;
	}

	public static PatternList buildPatternList(final String patternList, final String separator) {
		return new PatternList(patternList.split(separator));
	}
}
