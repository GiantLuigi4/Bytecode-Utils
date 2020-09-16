package com.tfc.bytecode.utils.asm;

import com.tfc.bytecode.utils.class_structure.MethodNode;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.util.List;

public class NodeBasedMethodVisitor extends MethodVisitor {
	private final MethodNode node;
	
	public NodeBasedMethodVisitor(int api, MethodNode nodes) {
		super(api);
		this.node = nodes;
	}
	
	public NodeBasedMethodVisitor(int api, MethodVisitor methodVisitor, MethodNode nodes) {
		super(api, methodVisitor);
		this.node = nodes;
	}
	
	@Override
	public void visitCode() {
		super.visitCode();
		for (AbstractInsnNode node1 : node.instructions) {
			switch (node1.getOpcode()) {
				case InsnNode.FIELD_INSN:
					super.visitFieldInsn(
							node1.getOpcode(),
							((FieldInsnNode) node1).owner,
							((FieldInsnNode) node1).name,
							((FieldInsnNode) node1).desc
					);
					break;
				case InsnNode.INVOKE_DYNAMIC_INSN:
					super.visitInvokeDynamicInsn(
							((InvokeDynamicInsnNode) node1).name,
							((InvokeDynamicInsnNode) node1).desc,
							((InvokeDynamicInsnNode) node1).bsm,
							((InvokeDynamicInsnNode) node1).bsmArgs
					);
					break;
				case InsnNode.METHOD_INSN:
					super.visitMethodInsn(
							node1.getOpcode(),
							((MethodInsnNode) node1).owner,
							((MethodInsnNode) node1).name,
							((MethodInsnNode) node1).desc,
							((MethodInsnNode) node1).itf
					);
					break;
				case InsnNode.INSN:
					super.visitInsn(0);
					break;
				case InsnNode.INT_INSN:
					super.visitIntInsn(node1.getOpcode(), ((IntInsnNode) node1).operand);
					break;
				case InsnNode.IINC_INSN:
					super.visitIincInsn(((IincInsnNode) node1).var, ((IincInsnNode) node1).incr);
					break;
				case InsnNode.JUMP_INSN:
					super.visitJumpInsn(node1.getOpcode(), ((JumpInsnNode) node1).label.getLabel());
					break;
				case InsnNode.LDC_INSN:
					super.visitLdcInsn(((LdcInsnNode) node1).cst);
					break;
				case InsnNode.VAR_INSN:
					super.visitVarInsn(node1.getOpcode(), ((VarInsnNode) node1).var);
					break;
				case InsnNode.LOOKUPSWITCH_INSN:
					List<Integer> integerList = ((LookupSwitchInsnNode) node1).keys;
					List<LabelNode> labelNodeList = ((LookupSwitchInsnNode) node1).labels;
					int[] ints = new int[integerList.size()];
					Label[] labels = new Label[labelNodeList.size()];
					for (int i = 0; i < integerList.size(); i++) ints[i] = integerList.get(i);
					for (int i = 0; i < labelNodeList.size(); i++) labels[i] = labelNodeList.get(i).getLabel();
					super.visitLookupSwitchInsn(((LookupSwitchInsnNode) node1).dflt.getLabel(), ints, labels);
					break;
				case InsnNode.TABLESWITCH_INSN:
					List<LabelNode> labelNodeList1 = ((TableSwitchInsnNode) node1).labels;
					Label[] labels1 = new Label[labelNodeList1.size()];
					for (int i = 0; i < labelNodeList1.size(); i++) labels1[i] = labelNodeList1.get(i).getLabel();
					super.visitTableSwitchInsn(((TableSwitchInsnNode) node1).min, ((TableSwitchInsnNode) node1).max, ((TableSwitchInsnNode) node1).dflt.getLabel(), labels1);
					break;
				case InsnNode.TYPE_INSN:
					super.visitTypeInsn(node1.getOpcode(), ((TypeInsnNode) node1).desc);
					break;
				case InsnNode.MULTIANEWARRAY_INSN:
					super.visitMultiANewArrayInsn(((MultiANewArrayInsnNode) node1).desc, ((MultiANewArrayInsnNode) node1).dims);
					break;
			}
		}
		super.visitEnd();
	}
}
