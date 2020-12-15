import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public class TruthTable {
	private String function;
	private Character[] characters;

	public TruthTable(String function) {
		this.function = function.replaceAll(" ", "");
		List<Character> list = new ArrayList<Character>();
		for (char c = 'a'; c <= 'z'; c++) {
			if (function.contains(Character.toString(c)) && !list.contains(c)) {
				list.add(c);
			}
		}
		list.sort((a, b) -> a - b);
		characters = list.toArray(Character[]::new);
		subAnd();
	}
	public TruthTable(String function, Character... characters) {
		this.function = function.replaceAll(" ", "");
		this.characters = characters;
		subAnd();
	}

	private void subAnd() {
		List<Character> chars = Arrays.asList(characters);
		List<Character> fn = function.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
		for (int i = 0; i < fn.size() - 1; i++) {
			if (chars.contains(fn.get(i)) && chars.contains(fn.get(i + 1))) {
				fn.add(i + 1, '*');
			} else if (i < fn.size() - 2 && chars.contains(fn.get(i)) && fn.get(i + 1).equals('\'') && chars.contains(fn.get(i + 2))) {
				fn.add(i + 2, '*');
			}
		}
		function = fn.stream().map(String::valueOf).collect(Collectors.joining(""));
	}

	private String calculate(String fn) {
		int result = 0;
		int a = Integer.parseInt(fn.substring(0, 1));
		char op = fn.charAt(1);
		int b = Integer.parseInt(fn.substring(2, 3));
		switch (op) {
			case '+':
				result = a | b;
				break;
			case '*':
				result = a & b;
				break;
		}
		return Integer.toString(result);
	}
	private String takeComplement(String fn) {
		int complement = 1;
		while (complement < fn.length()) {
			if (fn.charAt(complement) == '\'' && fn.charAt(complement - 1) != ')') {
				int value = fn.charAt(complement - 1) == '1' ? 0 : 1;
				fn = fn.substring(0, complement - 1) + value + fn.substring(complement + 1);
			}
			complement++;
		}
		return fn;
	}
	private String calculateParentheses(String fn) {
		int parentheses = fn.indexOf("(");
		while (parentheses != -1) {
			int opening = 1;
			int closing = 0;
			int index = parentheses + 1;
			while (opening != closing) {
				switch (fn.charAt(index)) {
					case '(':
						opening++;
						break;
					case ')':
						closing++;
						break;
				}
				index++;
			}
			fn = fn.substring(0, parentheses) + calculateFunction(fn.substring(parentheses + 1, index - 1)) + fn.substring(index);
			parentheses = fn.indexOf("(");
		}
		return fn;
	}
	private String calculateAnd(String fn) {
		int and = fn.indexOf("*");
		while (and != -1) {
			fn = fn.replaceFirst(".\\*.", calculate(fn.substring(and - 1, and + 2)));
			and = fn.indexOf("*");
		}
		return fn;
	}
	private String calculateOr(String fn) {
		int or = fn.indexOf("+");
		while (or != -1) {
			fn = fn.replaceFirst(".\\+.", calculate(fn.substring(or - 1, or + 2)));
			or = fn.indexOf("+");
		}
		return fn;
	}
	private String calculateFunction(String fn) {
		fn = takeComplement(fn);
		fn = calculateParentheses(fn);
		fn = takeComplement(fn);
		fn = calculateAnd(fn);
		fn = calculateOr(fn);
		if (fn.length() != 1) {
			fn = takeComplement(fn);
			fn = calculateParentheses(fn);
			fn = takeComplement(fn);
			fn = calculateAnd(fn);
			fn = calculateOr(fn);
		}
		return fn;
	}
	public void printSolution() {
		String str = "";
		for (char c : characters) {
			str += c + " ";
		}
		str += "| f\n";
		str += "-".repeat(characters.length * 2);
		str += "|---\n";
		int[] values = new int[characters.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = 1;
		}
		for (int i = 0; i < Math.pow(2, characters.length); i++) {
			String temp = function;
			for (int j = 0; j < characters.length; j++) {
				int switchLimit = (int) Math.pow(2, characters.length - j - 1);
				if (i % switchLimit == 0) {
					values[j] = values[j] == 0 ? 1 : 0;
				}
				temp = temp.replaceAll(Character.toString(characters[j]), Integer.toString(values[j]));
				str += values[j] + " ";
			}
			str += "| " + calculateFunction(temp) + "\n";
			calculateFunction(temp);
		}
		System.out.println(str);
	}
}