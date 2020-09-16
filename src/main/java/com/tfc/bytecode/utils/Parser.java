package com.tfc.bytecode.utils;

import com.tfc.bytecode.utils.class_structure.ClassNode;
import com.tfc.bytecode.utils.class_structure.ConstructorNodeSource;
import com.tfc.bytecode.utils.class_structure.FieldNodeSource;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	/**
	 * Parses a source file into a node like object, which all compilers will be able to use
	 *
	 * @param formattedSource {@link com.tfc.bytecode.utils.Formatter}
	 * @return An object containing the whole class's source code, in a org.ow2.asm node-like format that all compilers will be able to compile from (this might be a bit ambitious, but I'm still gonna try to do this)
	 */
	public static ClassNode parse(String formattedSource) {
		formattedSource = formattedSource.replace("\r\t\t", "");
		formattedSource = formattedSource.replace("\r\t", "");
		boolean inBody = false;
		boolean inConstructor = false;
		ArrayList<FieldNodeSource> fields = new ArrayList<>();
		ArrayList<MethodNodeSource> methods = new ArrayList<>();
		ArrayList<ConstructorNodeSource> constructors = new ArrayList<>();
		String packageName = "";
		String name = "";
		boolean inClass = false;
		int access = 0;
		String superName = "";
		ArrayList<String> interfaces = new ArrayList<>();
		StringBuilder body = new StringBuilder();
		for (String line : formattedSource.split("\n")) {
			if (!inClass) {
				if (line.startsWith("package ")) {
					packageName = line.replace("package ", "").replace(";", "") + ".";
				}
				if (line.contains("class")) {
					line = line.replace(", ", ",");
					String[] text = line.split(" ");
					if (text[0].equals("public")) {
						access = Access.parseAccess("public");
						name = text[2];
					} else
						name = text[1];
					boolean isExtend = false;
					boolean isInterf = false;
					for (String textS : text) {
						if (textS.equals("extends")) isExtend = true;
						else if (isExtend) {
							superName = textS;
							isExtend = false;
						} else if (textS.equals("implements")) isInterf = true;
						else if (isInterf) {
							interfaces.addAll(Arrays.asList(textS.split(",")));
							isInterf = false;
						}
					}
					inClass = true;
				}
			} else {
				if (line.charAt(0) == '\t' && line.charAt(1) != '\t') {
					if (line.endsWith("{")) {
						inBody = true;
						inConstructor = line.contains("\t" + name + "(");
						if (!inConstructor) {
							for (String modif : Access.modifs) {
								inConstructor = line.contains(modif + " " + name + "(");
								if (inConstructor) break;
							}
						}
					} else if (line.endsWith("}")) {
						inBody = false;
						if (!inConstructor)
							methods.add(new MethodNodeSource(body.toString() + "}"));
						else
							constructors.add(new ConstructorNodeSource(body.toString() + "}"));
						body = new StringBuilder();
					}
				}
				if (!inBody && line.endsWith(";")) {
					fields.add(new FieldNodeSource(line.replace("\t", "")));
				} else if (inBody) {
					while (line.startsWith("\t"))
						line = line.substring(1);
					body.append(line).append('\n');
				}
			}
		}
		return new ClassNode(packageName + name, superName, interfaces.toArray(new String[0]), access, fields, methods, constructors);
	}
}
