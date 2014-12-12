import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ole on 04/12/2014.
 */
public class Statement {

	private final String STMT;
	private static final String NUMBERS = " 0123456789.";
	private static final String OPERATORS1 = "+-";
	private static final String OPERATORS2 = "*/";
	private static final String OPERATORS = OPERATORS1 + OPERATORS2;
	private static final String VARIABLES = "xywh";
	private static final String OTHER = "()";
	private static final String ACCEPTED_CHARACTERS = NUMBERS + OPERATORS + VARIABLES + OTHER;

	public Statement(String stmt) {
		STMT = fullTrim(stmt);
	}

	public float parseStatement(int x, int y, int w, int h) {
		return parseStatement(STMT, x, y, w, h);
	}

	public float parseStatement(String stmt, int x, int y, int w, int h) {
		List<String> terms = getTerms(stmt);

		float result = 0;

		for(int p = 0; p < terms.size(); p++) {
			if(terms.get(p).charAt(0) == '(') {
				String term = terms.get(p);
				terms.set(p, "" + parseStatement(term.substring(1, term.length() - 1), x, y, w, h));
			}
		}

		for (int pos = 0; pos < terms.size(); pos++) {
			if (terms.get(pos).charAt(0) == 'x') terms.set(pos, "" + x);
			else if (terms.get(pos).charAt(0) == 'y') terms.set(pos, "" + y);
			else if (terms.get(pos).charAt(0) == 'w') terms.set(pos, "" + w);
			else if (terms.get(pos).charAt(0) == 'h') terms.set(pos, "" + h);
		}

		for (int i = 0; i < terms.size(); i++) {
			boolean isNextImportant = false;
			if (i < terms.size() - 2)
				isNextImportant = (terms.get(i + 2).charAt(0) == '*') || (terms.get(i + 2).charAt(0) == '/');
			char operator = terms.get(i).charAt(0);
			if (contains(OPERATORS2, operator)) {
				float a = Float.parseFloat(terms.get(i - 1));
				float b = Float.parseFloat(terms.get(i + 1));

				float temp_res = 0;

				if (operator == '*') {
					temp_res = a * b;
				} else if (operator == '/') {
					b = (b != 0 ? b : Float.MIN_VALUE);
					temp_res = a / b;
				}
				terms.remove(i + 1);
				terms.remove(i);
				terms.set(i - 1, "" + temp_res);

				if (isNextImportant) {
					i--;
				}
			}
		}

		for (int i = 1; i < terms.size(); i += 2) {
			char operator = terms.get(i - 1).charAt(0);
			if (operator == '+') {
				result += Float.parseFloat(terms.get(i));
			} else if (operator == '-') {
				result -= Float.parseFloat(terms.get(i));
			}
		}

		return result;
	}

	private List<String> getTerms(String stmt) {
		if (!contains(OPERATORS, stmt.charAt(0))) stmt = "+" + stmt;
		stmt = fullTrim(stmt);
		List<String> terms = new ArrayList<String>();

		boolean num = false;
		boolean insideParentheses = false;

		int pos = 0;
		int termPos = 0;

		for (int i = 0; i < stmt.length(); i++) {
			char c = stmt.charAt(i);

			if (!insideParentheses) {
				if (contains(VARIABLES, c)) {
					if (contains(NUMBERS, terms.get(termPos - 1).charAt(0)) || contains(VARIABLES, terms.get(termPos - 1).charAt(0))) {
						terms.add("*");
						termPos++;
					}
					terms.add("" + c);
					termPos++;

				} else if (contains(NUMBERS, c)) {
					if (!num) {
						num = true;
						pos = i;
					}
					if (i == stmt.length() - 1 || !contains(NUMBERS, stmt.charAt(i + 1))) {
						num = false;
						terms.add(stmt.substring(pos, i + 1));
						termPos++;
					}

				} else if(contains(OPERATORS, c)) {
					terms.add("" + c);
					termPos++;
				} else if(c == '('){
					pos = i;
					insideParentheses = true;
				}
			} else {
				if(c == ')') {
					terms.add(stmt.substring(pos, i+1));
					insideParentheses = false;
				}
			}
		}
		return terms;
	}

	public static boolean isValidStmt(String stmt) {
		if (stmt.length() == 0) {
			PixelArt.output("An empty function is not valid.", "ERROR");
			return false;
		}
		for (int i = 0; i < stmt.length(); i++) {
			if (ACCEPTED_CHARACTERS.indexOf(stmt.charAt(i)) < 0) {
				PixelArt.output("The function:\nColor = " + stmt + "\n is not valid.", "ERROR");
				return false;
			}
		}
		return true;
	}

	public static String fullTrim(String str) { // returns str without any spaces
		String result = "";
		for (int c = 0; c < str.length(); c++) {
			char charAt = str.charAt(c);
			if (charAt != ' ') result += charAt;
		}
		return result;
	}

	public static boolean contains(String str, char c) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) return true;
		}
		return false;
	}

	public String getStatement() {
		return STMT;
	}
}
