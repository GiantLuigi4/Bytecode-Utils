import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ConstructorUtils {

	static ClassReader reader = null;
	static ClassNode node = null;
	static ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

	public static void main(String[] args) throws IOException {
		reader = new ClassReader("EmptyClass");
		node = new ClassNode();
		reader.accept(node, 0);
		changeAccess(Opcodes.ACC_FINAL + Opcodes.ACC_PUBLIC);
		InsnList insns = new InsnList();
		insns.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insns.add(new LdcInsnNode("EEEEEEEEEEEEEEEEEEEEEEEEEEE"));
		insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
		//addInstructionsToStartOrEnd(insns, true);
		addInstructionsToStartOrEnd(insns, false);
		addInstructionsAfterOrBeforeInsn(insns, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"), 0, 1, false);
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

	public static void addInstructionsToStartOrEnd(InsnList list, boolean atStart) {
		for (MethodNode method : node.methods)
			if ((method.name.equals("<clinit>") || method.name.equals("<init>")) && method.desc.contains(")V")) {
				System.out.println(method.instructions.size());
				if (atStart)
					method.instructions.insert(list); //TODO FIX THIS, CUZ IT LITERALLY PUT INSNS BEFORE THE super() THING IN CONSTRUCTOR
				else
					method.instructions.insertBefore(new InsnNode(Opcodes.RETURN), list);
			}
	}

	public static void addInstructionsAfterOrBeforeInsn(InsnList listToAdd, AbstractInsnNode insn, int varInsnValue, int position, boolean before) { //I don't know if it works
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
						if (insnCounter == position)
							if (before)
								method.instructions.insertBefore(insn, listToAdd);
							else
								method.instructions.insert(insn, listToAdd);
					}
		
	}

}