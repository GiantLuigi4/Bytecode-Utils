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
}
