package com.github.lorenzopapi.asmutils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class MethodUtils {

	ClassReader reader;
	ClassNode node;

	public MethodUtils(byte[] array) {
		reader = new ClassReader(array);
		node = new ClassNode();
		reader.accept(node, 0);
	}

	/*public void main(String[] args) throws IOException {
		InsnList insns = new InsnList();
		insns.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns.add(new LdcInsnNode("EEEEEEEEEEEEEEEEEEEEEEEEEEE"));
		insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		changeMethodAccess(ACC_PRIVATE, "hello", "()I");
		InsnList insns1 = new InsnList();
		insns1.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns1.add(new LdcInsnNode("e"));
		insns1.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		InsnList insns2 = new InsnList();
		insns2.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns2.add(new LdcInsnNode("e"));
		insns2.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		InsnList insns3 = new InsnList();
		insns3.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns3.add(new LdcInsnNode("a d d e d"));
		insns3.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		addInstructionsToStartOrEnd("hello", "()I", insns, true);
		addMethodToClass(ACC_PUBLIC, "theWorld", "()V", null, null, insns2);
		addInstructionsAfterOrBeforeInsn("theWorld", "()V", insns3, GETSTATIC, 1, 0, true);
		byte[] bytes = addInstructionsToStartOrEnd("hello", "()I", insns1, false);
		FileOutputStream stream = new FileOutputStream("yes.class");
		stream.write(bytes);
		stream.close();
	}*/

	/**
	 * Changes a method access
	 * @param newAccess new access of the method
	 * @param methodName name of the method
	 * @param descriptor descriptor of the method
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] changeMethodAccess(int newAccess, String methodName, String descriptor) {
		for (MethodNode method : node.methods)
			if (method.name.equals(methodName) && method.desc.equals(descriptor))
				method.access = newAccess;

		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	/**
	 * Adds the specified InsnList at the start or at the end of the method
	 * @param name name of the method
	 * @param descriptor descriptor of the method
	 * @param list InsnList to add
	 * @param atStart insert list at the start?
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] addInstructionsToStartOrEnd(String name, String descriptor, InsnList list, boolean atStart) {
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

	/**
	 * Adds a method to the class
	 * @param access access of the method
	 * @param name name of the method
	 * @param descriptor descriptor of the method
	 * @param signature signature of the method, can be {@literal null}
	 * @param ex exceptions (if any) of the method, can be {@literal null}
	 * @param instructions InsnList of the method's instructions
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] addMethodToClass(int access, String name, String descriptor, String signature, String[] ex, InsnList instructions) {
		MethodNode method = new MethodNode(access, name, descriptor, signature, ex);
		method.instructions = instructions;
		node.methods.add(method);
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	/**
	 * Adds an InsnList before or after a specified OpCode at a certain position
	 * @param name name of the method
	 * @param desc descriptor of the method
	 * @param listToAdd InsnList to add
	 * @param opCodeToSearch the OpCode you are searching for
	 * @param position the nth position of the OpCode (e.g. if there are 3 ICONST_2 and you want to insert instructions after or before the first, pass 1 as position)
	 * @param varInsnValue used for Instructions like ALOAD, ASTORE which require a value. Just pass 0 if you aren't searching for a Var OpCode
	 * @param before insert list before the OpCode's instruction?
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] addInstructionsAfterOrBeforeInsn(String name, String desc, InsnList listToAdd, int opCodeToSearch, int position, int varInsnValue, boolean before) {
		int insnCounter = 0;

		for (MethodNode method : node.methods)
			if (method.name.equals(name) && method.desc.equals(desc))
				for (AbstractInsnNode actualInstruction : method.instructions)
					if (actualInstruction.getOpcode() == opCodeToSearch) {
						if ((opCodeToSearch >= 21 && opCodeToSearch <= 25) || (opCodeToSearch >= 54 && opCodeToSearch <= 58) || opCodeToSearch == 169) {
							VarInsnNode varNode = (VarInsnNode) actualInstruction;
							if (varNode.var == varInsnValue)
								insnCounter++;
						} else {
							insnCounter++;
						}
						if (insnCounter == position) {
							if (before) {
								method.instructions.insertBefore(actualInstruction, listToAdd);
							} else {
								method.instructions.insert(actualInstruction, listToAdd);
							}
						}
					}
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	//no javadoc cuz it's private :D
	private int parseReturnTypeFromDesc(String desc) {
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
