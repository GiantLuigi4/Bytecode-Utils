package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;

public class ConstructorNodeSource {
	public final String code;
	
	public ConstructorNodeSource(String code) {
		this.code = code;
	}
	
	public ConstructorNodeSource(String access, String name, String code) {
		this.code = access + name + "{" + code + "}";
	}
	
	public ConstructorNodeSource(int access, String name, String code) {
		this.code = Access.parseAccess(access) + name + "{" + code + "}";
	}
	
	public ConstructorNodeSource(String access, String returnVal, String name, String code) {
		this.code = access + returnVal + name + "{" + code + "}";
	}
	
	public ConstructorNodeSource(int access, String returnVal, String name, String code) {
		this.code = Access.parseAccess(access) + returnVal + name + "{" + code + "}";
	}
}
