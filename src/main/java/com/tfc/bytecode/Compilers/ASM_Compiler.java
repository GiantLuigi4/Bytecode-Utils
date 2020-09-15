package com.tfc.bytecode.Compilers;

import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class ASM_Compiler {
	public ASM_Compiler() {
	}
	
	public byte[] generate(String name, int access, String superName, String[] interfaces, ArrayList<FieldNode> nodesF, ArrayList<MethodNode> nodesM) {
		ClassWriter writer = new ClassWriter(Opcodes.ASM8);
		if (superName.equals("")) superName = "java/lang/Object";
		writer.visit(8, access, name, "none", superName, interfaces);
		writer.visitSource(name.replace(".", "/") + ".java", null);
		for (FieldNode node : nodesF)
			writer.visitField(node.access, node.name, node.desc, node.signature, node.value);
		for (MethodNode node : nodesM) {
			MethodVisitor visitor = writer.visitMethod(node.access, node.name, node.desc, node.signature, node.exceptions);
			visitor.visitCode();
			for (AbstractInsnNode node1 : node.instructions) {
				switch (node1.getOpcode()) {
					case InsnNode.FIELD_INSN:
						visitor.visitFieldInsn(
								node1.getOpcode(),
								((FieldInsnNode) node1).owner,
								((FieldInsnNode) node1).name,
								((FieldInsnNode) node1).desc
						);
						break;
					case InsnNode.INVOKE_DYNAMIC_INSN:
						visitor.visitInvokeDynamicInsn(
								((InvokeDynamicInsnNode) node1).name,
								((InvokeDynamicInsnNode) node1).desc,
								((InvokeDynamicInsnNode) node1).bsm,
								((InvokeDynamicInsnNode) node1).bsmArgs
						);
						break;
					case InsnNode.METHOD_INSN:
						visitor.visitMethodInsn(
								node1.getOpcode(),
								((MethodInsnNode) node1).owner,
								((MethodInsnNode) node1).name,
								((MethodInsnNode) node1).desc,
								((MethodInsnNode) node1).itf
						);
						break;
					case InsnNode.INSN:
						visitor.visitInsn(0);
						break;
					case InsnNode.INT_INSN:
						visitor.visitIntInsn(node1.getOpcode(), ((IntInsnNode) node1).operand);
						break;
					case InsnNode.IINC_INSN:
						visitor.visitIincInsn(((IincInsnNode) node1).var, ((IincInsnNode) node1).incr);
						break;
					case InsnNode.JUMP_INSN:
						visitor.visitJumpInsn(node1.getOpcode(), ((JumpInsnNode) node1).label.getLabel());
						break;
					case InsnNode.LDC_INSN:
						visitor.visitLdcInsn(((LdcInsnNode) node1).cst);
						break;
					case InsnNode.VAR_INSN:
						visitor.visitVarInsn(node1.getOpcode(), ((VarInsnNode) node1).var);
						break;
					case InsnNode.LOOKUPSWITCH_INSN:
						List<Integer> integerList = ((LookupSwitchInsnNode) node1).keys;
						List<LabelNode> labelNodeList = ((LookupSwitchInsnNode) node1).labels;
						int[] ints = new int[integerList.size()];
						Label[] labels = new Label[labelNodeList.size()];
						for (int i = 0; i < integerList.size(); i++) ints[i] = integerList.get(i);
						for (int i = 0; i < labelNodeList.size(); i++) labels[i] = labelNodeList.get(i).getLabel();
						visitor.visitLookupSwitchInsn(((LookupSwitchInsnNode) node1).dflt.getLabel(), ints, labels);
						break;
					case InsnNode.TABLESWITCH_INSN:
						List<LabelNode> labelNodeList1 = ((TableSwitchInsnNode) node1).labels;
						Label[] labels1 = new Label[labelNodeList1.size()];
						for (int i = 0; i < labelNodeList1.size(); i++) labels1[i] = labelNodeList1.get(i).getLabel();
						visitor.visitTableSwitchInsn(((TableSwitchInsnNode) node1).min, ((TableSwitchInsnNode) node1).max, ((TableSwitchInsnNode) node1).dflt.getLabel(), labels1);
						break;
					case InsnNode.TYPE_INSN:
						visitor.visitTypeInsn(node1.getOpcode(), ((TypeInsnNode) node1).desc);
						break;
					case InsnNode.MULTIANEWARRAY_INSN:
						visitor.visitMultiANewArrayInsn(((MultiANewArrayInsnNode) node1).desc, ((MultiANewArrayInsnNode) node1).dims);
						break;
				}
				visitor.visitEnd();
			}
		}
		writer.visitEnd();
		return writer.toByteArray();
	}
}
