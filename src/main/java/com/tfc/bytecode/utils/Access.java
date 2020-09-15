package com.tfc.bytecode.utils;

import java.lang.reflect.Modifier;

public class Access {
	public static final String[] modifs = new String[]{
			"public",
			"private",
			"protected",
			"static",
			"final",
			"transient",
			"strictfp",
			"interface",
			"abstract",
			"native"
	};
	
	public static String parseAccess(int accessl) {
		String access = "";
		if (Modifier.isPublic(accessl))
			access += "public ";
		else if (Modifier.isPrivate(accessl))
			access += "private ";
		else if (Modifier.isProtected(accessl))
			access += "protected ";
		if (Modifier.isStatic(accessl))
			access += "static ";
		if (Modifier.isFinal(accessl))
			access += "final ";
		if (Modifier.isTransient(accessl))
			access += "transient ";
		if (Modifier.isNative(accessl))
			access += "native ";
		if (Modifier.isAbstract(accessl))
			access += "abstract ";
		if (Modifier.isStrict(accessl))
			access += "strictfp ";
		if (Modifier.isInterface(accessl))
			access += "interface ";
		return access;
	}
	
	public static int parseAccess(String accessl) {
		int lvl = 0;
		if (accessl.contains("public"))
			lvl = Modifier.PUBLIC;
		else if (accessl.contains("private"))
			lvl = Modifier.PRIVATE;
		else if (accessl.contains("protected"))
			lvl = Modifier.PROTECTED;
		if (accessl.contains("static"))
			lvl = lvl | Modifier.STATIC;
		if (accessl.contains("final"))
			lvl = lvl | Modifier.FINAL;
		if (accessl.contains("transient"))
			lvl = lvl | Modifier.TRANSIENT;
		if (accessl.contains("native"))
			lvl = lvl | Modifier.NATIVE;
		if (accessl.contains("interface"))
			lvl = lvl | Modifier.INTERFACE;
		if (accessl.contains("strictfp"))
			lvl = lvl | Modifier.STRICT;
		if (accessl.contains("abstract"))
			lvl = lvl | Modifier.ABSTRACT;
		return lvl;
	}
}
