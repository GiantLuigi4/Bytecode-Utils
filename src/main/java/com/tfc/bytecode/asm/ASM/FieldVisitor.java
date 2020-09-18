package com.tfc.bytecode.asm.ASM;

import org.objectweb.asm.ClassVisitor;

public class FieldVisitor extends ClassVisitor {
	public final String name;
	public final int level;
	public final Object value;
	public final String desc;
	
	public FieldVisitor(int api, String name, int level, Object value, String desc) {
		super(api);
		this.name = name;
		this.level = level;
		this.value = value;
		this.desc = desc;
	}
	
	public FieldVisitor(int api, ClassVisitor classVisitor, String name, int level, Object value, String desc) {
		super(api, classVisitor);
		this.name = name;
		this.level = level;
		this.value = value;
		this.desc = desc;
	}
	
	@Override
	public org.objectweb.asm.FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		if (name.equals(this.name)) {
			Object val = this.value != null ? this.value : value;
			String desc = this.desc != null ? this.desc : descriptor;
			return super.visitField(level, name, desc, signature, val);
		}
		return super.visitField(access, name, descriptor, signature, value);
	}
}
