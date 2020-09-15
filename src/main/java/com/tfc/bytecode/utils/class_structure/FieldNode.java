package com.tfc.bytecode.utils.class_structure;

public class FieldNode {
	public FieldNode(int access, String name, String desc, String signature, Object value) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.value = value;
	}
	
	public final int access;
	public final String name;
	public final String desc;
	public final String signature;
	public final Object value;
}
