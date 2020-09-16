package com.tfc.bytecode.utils;

public class Formatter {
	/**
	 * Formats a java file into something usable by the Parser
	 *
	 * @param src The raw source code
	 * @return A formatted version of the code
	 */
	public static String formatForCompile(String src) {
		StringBuilder lineCommentRemover = new StringBuilder();
		for (String s : src.split("\n")) {
			boolean inString = false;
			boolean escaped = false;
			boolean slash = false;
			for (char c : s.toCharArray()) {
				if (escaped) {
					escaped = false;
				} else if (c == '"') {
					inString = !inString;
				} else if (c == '\\') {
					escaped = true;
				} else {
					if (c == '/') {
						if (slash) break;
						slash = true;
					} else {
						if (slash) {
							lineCommentRemover.append('/');
							slash = false;
						}
						lineCommentRemover.append(c);
					}
				}
			}
			lineCommentRemover.append('\n');
		}
		src = lineCommentRemover.toString();
		StringBuilder builder = new StringBuilder();
		boolean inString = false;
		boolean escaped = false;
		int brackets = 0;
		src = src.replace("\n", "");
		src = src.replace("\r", "");
		src = src.replace("\t", "");
		for (int i = 0; i < src.length(); i++) {
			if (inString) {
				builder.append(src.charAt(i));
				if (src.charAt(i) == '"') inString = false;
			} else if (escaped) {
				builder.append(src.charAt(i));
				escaped = false;
			} else {
				if (src.charAt(i) == '\\') {
					escaped = true;
				} else if (src.charAt(i) == '"') {
					builder.append(src.charAt(i));
					inString = true;
				} else {
					if ((src.length() > i + 1 && src.charAt(i + 1) == '}')) {
						brackets--;
					}
					if (src.charAt(i) == '{' || src.charAt(i) == ';') {
						builder.append(src.charAt(i));
						builder.append('\n');
						if (src.charAt(i) == '{') {
							brackets++;
						}
						for (int i1 = 0; i1 < brackets; i1++) {
							builder.append("\t");
						}
					} else if (src.charAt(i) != '\n' && src.charAt(i) != '\t') {
						builder.append(src.charAt(i));
					}
					if ((src.charAt(i) == '}')) {
						builder.append('\n');
						for (int i1 = 0; i1 < brackets; i1++) {
							builder.append("\t");
						}
					}
				}
			}
		}
		return builder.toString();
	}
}
