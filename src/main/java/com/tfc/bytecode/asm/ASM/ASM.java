package com.tfc.bytecode.asm.ASM;

import com.github.lorenzopapi.asmutils.ConstructorUtils;
import com.github.lorenzopapi.asmutils.MethodUtils;
import com.tfc.bytecode.utils.Access;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;

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
	
	public ASM transformMethod(String name, String descriptor, String newAccess) {
		MethodUtils utils = new MethodUtils(thisClass);
		thisClass = utils.changeMethodAccess(Access.parseAccess(newAccess), name, descriptor);
		return this;
	}
	
	public ASM transformMethod(String name, String descriptor, InsnList list, boolean atStart) {
		MethodUtils utils = new MethodUtils(thisClass);
		thisClass = utils.addInstructionsToStartOrEnd(name, descriptor, list, atStart);
		return this;
	}
	
	public ASM transformMethod(String name, String descriptor, InsnList list, int search, int pos, int insnVal, boolean before) {
		MethodUtils utils = new MethodUtils(thisClass);
		thisClass = utils.addInstructionsAfterOrBeforeInsn(name, descriptor, list, search, pos, insnVal, before);
		return this;
	}
	
	public ASM addMethod(String access, String name, String descriptor, String signature, String[] exceptions, InsnList method) {
		MethodUtils utils = new MethodUtils(thisClass);
		thisClass = utils.addMethodToClass(Access.parseAccess(access), name, descriptor, signature, exceptions, method);
		return this;
	}
	
	public ASM transformConstructor(String descriptor, String newAccess) {
		ConstructorUtils utils = new ConstructorUtils(thisClass);
		thisClass = utils.changeAccess(Access.parseAccess(newAccess), descriptor);
		return this;
	}
	
	public byte[] toBytes() {
		return thisClass;
	}
}
