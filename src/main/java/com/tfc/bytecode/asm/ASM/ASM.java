package com.tfc.bytecode.asm.ASM;

import com.tfc.bytecode.utils.Access;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ASM {
	private byte[] thisClass;
	
	public ASM(byte[] thisClass) {
		this.thisClass = thisClass;
	}
	
	public ASM addField(String name, String access, String desc, Object val) {
		ClassReader reader = new ClassReader(thisClass);
		ClassWriter writer = new ClassWriter(reader, Opcodes.ASM8);
		FieldVisitor visitor = new FieldVisitor(Opcodes.ASM8, writer, name, Access.parseAccess(access), val, desc);
		reader.accept(visitor, ClassWriter.COMPUTE_FRAMES);
		visitor.visitField(0, name, null, null, null);
		writer.visitEnd();
		thisClass = writer.toByteArray();
		return this;
	}
	
	public ASM transformField(String name, String access) {
		ClassReader reader = new ClassReader(thisClass);
		ClassWriter writer = new ClassWriter(reader, Opcodes.ASM8);
		FieldVisitor visitor = new FieldVisitor(Opcodes.ASM8, writer, name, Access.parseAccess(access), null, null);
		reader.accept(visitor, ClassWriter.COMPUTE_FRAMES);
		writer.visitEnd();
		thisClass = writer.toByteArray();
		return this;
	}
	
	public byte[] toBytes() {
		return thisClass;
	}
}
