package com.tfc.bytecode.utils.class_structure;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ArrayList;

public class MethodNode {
	public MethodNode(int access, String name, String desc, String signature, String[] exceptions) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.exceptions = exceptions;
	}
	
	public final int access;
	public final String name;
	public final String desc;
	public final String signature;
	public final String[] exceptions;
	public ArrayList<AbstractInsnNode> instructions = new ArrayList<>();
	
	public void addInstruction(AbstractInsnNode node) {
		instructions.add(node);
	}
}
