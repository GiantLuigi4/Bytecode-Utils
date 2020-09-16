package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;

public class MethodNodeSource {
	public final String code;
	
	public MethodNodeSource(String code) {
		this.code = code;
	}
	
	public MethodNodeSource(String access, String name, String code) {
		this.code = access + name + "{" + code + "}";
	}
	
	public MethodNodeSource(int access, String name, String code) {
		this.code = Access.parseAccess(access) + name + "{" + code + "}";
	}
	
	public MethodNodeSource(String access, String returnVal, String name, String code) {
		this.code = access + returnVal + name + "{" + code + "}";
	}
	
	public MethodNodeSource(int access, String returnVal, String name, String code) {
		this.code = Access.parseAccess(access) + returnVal + name + "{" + code + "}";
	}
	
	public String getType() {
		String type = code;
		for (String modif : Access.modifs)
			type = type.replace(modif, "");
		type = (type.trim());
		return type.substring(0, type.indexOf(" "));
	}
	
	public String getName() {
		String name = code;
		for (String modif : Access.modifs)
			name = name.replace(modif, "");
		name = name.replace(getType() + " ", "");
		name = name.trim();
		name = name.substring(0, name.indexOf("("));
		if (name.contains(" "))
			return name.substring(0, name.indexOf(" "));
		else if (name.contains(";"))
			return name.substring(0, name.indexOf(";"));
		else return name;
	}
}
