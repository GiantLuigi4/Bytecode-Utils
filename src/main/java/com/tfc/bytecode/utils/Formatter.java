package com.tfc.bytecode.utils;

public class Formatter {
	public static String formatForCompile(String src) {
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
