package com.tfc.bytecode.asm.ASM;

import com.tfc.bytecode.utils.Access;
import org.objectweb.asm.ClassVisitor;

public class FieldVisitor extends ClassVisitor {
	public final String name;
	public final int level;
	
	public FieldVisitor(int api, ClassVisitor classVisitor, int level, String name) {
		super(api, classVisitor);
		this.level = level;
		this.name = name;
	}
	
	public FieldVisitor(int api, ClassVisitor classVisitor, String level, String name) {
		super(api, classVisitor);
		this.level = Access.parseAccess(level);
		this.name = name;
	}
	
	@Override
	public org.objectweb.asm.FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		return super.visitField(level, name, descriptor, signature, value);
	}
}
