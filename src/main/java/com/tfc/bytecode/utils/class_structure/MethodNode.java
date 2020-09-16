package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class MethodNode {
	public MethodNode(int access, String name, String desc, String signature, String[] exceptions) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.exceptions = exceptions;
	}
	
	public int maxStack;
	public int maxLocals;
	public String desc;
	public final int access;
	public final String name;
	public ArrayList<InsnNode> instructions = new ArrayList<>();
	public final String signature;
	public final String[] exceptions;
	
	public MethodNode(MethodNodeSource source) {
		this.access = Access.parseAccess(source.code.substring(0, source.code.indexOf(" " + source.getType())));
		this.name = source.getName();
		this.desc = source.getType().replace(".", "/");
		this.signature = null;
		this.exceptions = null;
		for (String line : source.code.substring(source.code.indexOf("{") + 1).split(";")) {
			if (line.trim().startsWith("return") && line.contains("+")) {
				addInstruction(new InsnNode("VarInsn", new Object[]{Opcodes.ILOAD, 1}));
				addInstruction(new InsnNode("VarInsn", new Object[]{Opcodes.ILOAD, 2}));
				addInstruction(new InsnNode("Insn", new Object[]{Opcodes.IADD}));
				addInstruction(new InsnNode("Insn", new Object[]{Opcodes.IRETURN}));
			} else if (line.trim().startsWith("return")) {
				addInstruction(new InsnNode(InsnNode.InsnType.INSN, new Object[]{Opcodes.IRETURN}));
			}
		}
	}
	
	public void addInstruction(InsnNode node) {
		instructions.add(node);
	}
}
