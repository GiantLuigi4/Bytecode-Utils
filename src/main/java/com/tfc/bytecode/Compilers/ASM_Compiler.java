package com.tfc.bytecode.Compilers;

import com.tfc.bytecode.utils.asm.NodeBasedMethodVisitor;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class ASM_Compiler {
	public ASM_Compiler() {
	}
	
	//https://www.beyondjava.net/quick-guide-writing-byte-code-asm
	public byte[] generate(String name, int access, String superName, String[] interfaces, ArrayList<FieldNode> nodesF, ArrayList<MethodNode> nodesM) {
		ClassWriter writer = new ClassWriter(Opcodes.ASM8);
		if (superName.equals("")) superName = "java/lang/Object";
		writer.visit(8, access, name, null, superName, interfaces);
		writer.visitSource(name.replace(".", "/") + ".java", null);
		for (FieldNode node : nodesF)
			writer.visitField(node.access, node.name, node.desc, node.signature, node.value);
		for (MethodNode node : nodesM) {
			MethodVisitor visitor = writer.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions);
			new NodeBasedMethodVisitor(Opcodes.ASM8, visitor, node).visitCode();
		}
		writer.visitEnd();
		return writer.toByteArray();
	}
}
