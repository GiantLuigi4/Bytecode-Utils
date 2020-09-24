package com.tfc.bytecode.utils.class_structure;

public class GenericInsnNode {
	public final InsnType type;
	public final Object[] args;
	
	public GenericInsnNode(String type, Object[] args) {
		this.type = InsnType.get(type);
		this.args = args;
	}
	
	public GenericInsnNode(InsnType type, Object[] args) {
		this.type = type;
		this.args = args;
	}
	
	public enum InsnType {
		INSN("Insn"),
		VAR_INSN("VarInsn"),
		JUMP_INSN("JumpInsn"),
		LABEL("Label"),
		METHOD_INSN("MethodInsn"),
		;
		
		public final String type;
		
		InsnType(String type) {
			this.type = type;
		}
		
		public static InsnType get(String text) {
			for (InsnType type : values())
				if (type.type.equals(text)) return type;
			return valueOf(text.toUpperCase());
		}
	}
}
