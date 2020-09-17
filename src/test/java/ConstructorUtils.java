import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

//Looks like everything works
public class ConstructorUtils {

	static ClassReader reader = null;
	static ClassNode node = null;
	static ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

	public static void main(String[] args) throws IOException {
		reader = new ClassReader("EmptyClass");
		node = new ClassNode();
		reader.accept(node, 0);
		changeAccess(ACC_FINAL + ACC_PUBLIC);
		InsnList insns = new InsnList();
		insns.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns.add(new LdcInsnNode("EEEEEEEEEEEEEEEEEEEEEEEEEEE"));
		insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		InsnList insns1 = new InsnList();
		insns1.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns1.add(new LdcInsnNode("THE WORLD"));
		insns1.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		InsnList insns2 = new InsnList();
		insns1.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns1.add(new LdcInsnNode("THE WORLD"));
		insns1.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		addInstructionsToStartOrEnd(insns, "", true);
		addInstructionsToStartOrEnd(insns1, "", false);
		InsnNode beforeNode = new InsnNode(RETURN); //new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		addInstructionsAfterOrBeforeInsn(insns2, beforeNode, 1, 0, true);
		node.accept(result);
		FileOutputStream stream = new FileOutputStream("yes.class");
		stream.write(result.toByteArray());
		stream.close();
	}

	public static void changeAccess(int newAccess) {
		for (MethodNode method : node.methods)
			if ((method.name.equals("<clinit>") || method.name.equals("<init>")) && method.desc.contains(")V"))
				method.access = newAccess;
	}

	public static void addInstructionsToStartOrEnd(InsnList list, String descriptor, boolean atStart) {
		if (descriptor.equals("")) {
			descriptor = "()V";
		}
		for (MethodNode method : node.methods)
			if ((method.name.equals("<clinit>") || method.name.equals("<init>")) && method.desc.contains(descriptor)) {
				for (AbstractInsnNode node : method.instructions) {
					List<Integer> opcodesList = new ArrayList<>();
					method.instructions.forEach((absNode) -> opcodesList.add(absNode.getOpcode()));
					if (atStart) {
						if (opcodesList.contains(INVOKESPECIAL)) {
							if (node.getOpcode() == INVOKESPECIAL) {
								method.instructions.insert(node, list);
							}
						} else {
							method.instructions.insert(list);
						}
					} else if (node.getOpcode() == RETURN) {
						method.instructions.insertBefore(node, list);
					}
				}
			}
	}

	public static void addInstructionsAfterOrBeforeInsn(InsnList listToAdd, AbstractInsnNode insn, int position, int varInsnValue, boolean before) { //I don't know if it works
		int insnCounter = 0;

		for (MethodNode method : node.methods)
			if ((method.name.equals("<clinit>") || method.name.equals("<init>")) && method.desc.contains(")V"))
				for (AbstractInsnNode insnNode : method.instructions)
					if (insnNode.getOpcode() == insn.getOpcode()) {
						if (insn instanceof VarInsnNode) {
							VarInsnNode varNode = (VarInsnNode) insn;
							if (varNode.var == varInsnValue)
								insnCounter++;
						} else {
							insnCounter++;
						}
						if (insnCounter == position) {
							if (before) {
								method.instructions.insertBefore(insn, listToAdd);
							} else {
								method.instructions.insert(insn, listToAdd);
							}
						}
					}

	}

}