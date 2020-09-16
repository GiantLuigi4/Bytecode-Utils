package com.tfc.bytecode.utils;

public class Descriptor {
	public static String getDescriptorFor(String clazz, boolean isMethod) {
		String str;
		if (clazz.equals("int")) str = "I";
		else if (clazz.equals("long")) str = "J";
		else if (isMethod) str = "L" + (clazz.replace(".", "/")) + ";";
		else str = clazz.replace(".", "/");
		return str;
	}
}
