package com.tfc.bytecode.compilers;

import com.tfc.bytecode.utils.Formatter;
import com.tfc.bytecode.utils.Parser;
import com.tfc.bytecode.utils.class_structure.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class ASM_Compiler {
	public ASM_Compiler() {
	}
	
	//https://www.beyondjava.net/quick-guide-writing-byte-code-asm
	public byte[] compile(String name, int access, String superName, String[] interfaces, ArrayList<FieldNode> nodesF, ArrayList<MethodNode> nodesM) {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		if (superName.equals("")) superName = "java/lang/Object";
		writer.visit(Opcodes.V1_8, access, name, null, superName, interfaces);
		writer.visitSource(name.replace(".", "/") + ".java", null);
		for (FieldNode node : nodesF) {
			String desc;
			if (node.desc.equals("int")) desc = "I";
			else desc = node.desc;
			writer.visitField(node.access, node.name, desc, null, node.value);
		}
		for (MethodNode node : nodesM) {
			MethodVisitor visitor = writer.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions);
			visitor.visitCode();
			for (GenericInsnNode node1 : node.instructions) {
				switch (node1.type) {
					case INSN:
						visitor.visitInsn((int) node1.args[0]);
						break;
					case VAR_INSN:
						visitor.visitVarInsn((int) node1.args[0], (int) node1.args[1]);
						break;
					case JUMP_INSN:
						visitor.visitJumpInsn((int) node1.args[0], (Label) node1.args[1]);
						break;
					case LABEL:
						visitor.visitLabel((Label) node1.args[0]);
						break;
					case METHOD_INSN:
						visitor.visitMethodInsn((int) node1.args[0], (String) node1.args[1], (String) node1.args[2], (String) node1.args[3], (boolean) node1.args[4]);
						break;
				}
			}
			visitor.visitMaxs(node.maxStack, node.maxLocals);
		}
		throw new RuntimeException(new IllegalAccessException("NYI"));
//		return writer.toByteArray();
	}
	
	public byte[] compile(ClassNode node) {
		ArrayList<FieldNode> fieldNodes = new ArrayList<>();
		for (FieldNodeSource fNode : node.fields)
			new FieldNode(fNode);
		ArrayList<MethodNode> nodes = new ArrayList<>();
		for (MethodNodeSource mNode : node.methods)
			nodes.add(new MethodNode(mNode));
		return compile(node.name, node.modifs, node.superName, node.interfaces, fieldNodes, nodes);
	}
	
	public byte[] compile(String src) {
		src = Formatter.formatForCompile(src);
		ClassNode node = Parser.parse(src);
		return compile(node);
	}
}
