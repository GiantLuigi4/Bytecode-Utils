package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;
import com.tfc.bytecode.utils.Descriptor;

public class FieldNode {
	public FieldNode(int access, String name, String desc, String signature, Object value) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.value = value;
	}
	
	public FieldNode(FieldNodeSource source) {
		this.access = Access.parseAccess(source.code.substring(0, source.code.indexOf(" " + source.getType())));
		this.name = source.getName();
		this.desc = Descriptor.getDescriptorFor(source.getType(), false);
		this.signature = null;
		//Can't really parse the value out of a source code tbh
		this.value = null;
	}
	
	public final int access;
	public final String name;
	public final String desc;
	public final String signature;
	public final Object value;
}
