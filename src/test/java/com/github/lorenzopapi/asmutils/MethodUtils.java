package com.github.lorenzopapi.asmutils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

//I just need to be able to add instructions in the middle of the method
//but I gtg now
//I love this convos w/comments lmao
public class MethodUtils {

	static ClassReader reader = null;
	static ClassNode node = null;

	public static void main(String[] args) throws IOException {
		reader = new ClassReader("EmptyClass");
		node = new ClassNode();
		reader.accept(node, 0);
		InsnList insns = new InsnList();
		insns.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns.add(new LdcInsnNode("EEEEEEEEEEEEEEEEEEEEEEEEEEE"));
		insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		changeMethodAccess(ACC_PRIVATE, "hello", "()I");
		InsnList insns1 = new InsnList();
		insns1.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns1.add(new LdcInsnNode("e"));
		insns1.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		addInstructionsToStartOrEnd("hello", "()I", insns, true);
		byte[] bytes = addInstructionsToStartOrEnd("hello", "()I", insns1, false);
		FileOutputStream stream = new FileOutputStream("yes.class");
		stream.write(bytes);
		stream.close();
	}
	
	public static byte[] changeMethodAccess(int newAccess, String methodName, String descriptor) {
		for (MethodNode method : node.methods)
			if (method.name.equals(methodName) && method.desc.equals(descriptor))
				method.access = newAccess;

		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	public static byte[] addInstructionsToStartOrEnd(String name, String descriptor, InsnList list, boolean atStart) {
		if (descriptor.equals("")) {
			descriptor = "()V";
		}
		for (MethodNode method : node.methods)
			if (method.name.equals(name) && method.desc.equals(descriptor)) {
				if (atStart) {
					method.instructions.insert(list);
				} else {
					String finalDescriptor = descriptor;
					method.instructions.forEach((node) -> {
						if (node.getOpcode() == parseReturnTypeFromDesc(finalDescriptor))
							method.instructions.insertBefore(node, list);
					});
				}
			}
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	public static int parseReturnTypeFromDesc(String desc) {
		String returnString = desc.split("\\)")[1];
		switch (returnString) {
			case "V":
				return RETURN;
			case "I":
				return IRETURN;
			case "F":
				return FRETURN;
			case "J":
				return LRETURN;
			case "D":
				return DRETURN;
			default:
				return ARETURN;
		}
	}
}
