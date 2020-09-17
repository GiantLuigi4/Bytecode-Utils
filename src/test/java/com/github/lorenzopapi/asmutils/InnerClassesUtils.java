package com.github.lorenzopapi.asmutils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;

public class InnerClassesUtils {

	ClassReader reader;
	ClassNode node;

	public InnerClassesUtils(byte[] array) {
		reader = new ClassReader(array);
		node = new ClassNode();
		reader.accept(node, 0);
	}

	/*public void main(String[] args) throws IOException {
		addInnerClass("EmptyClass$NewInner", ACC_PRIVATE);
		byte[] bytes = changeInnerClassAccess(ACC_PUBLIC, "InnerClass");
		FileOutputStream stream = new FileOutputStream("yes.class");
		stream.write(bytes);
		stream.close();
	}*/

	/**
	 * Changes an inner class's access
	 * @param newAccess new access of the inner class
	 * @param className name of the inner class
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] changeInnerClassAccess(int newAccess, String className) {
		for (InnerClassNode innerClass : node.innerClasses) {
			if (innerClass.innerName.equals(className))
				innerClass.access = newAccess;
		}
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	/**
	 * Adds an inner class
	 * @param fullName fullname of the class, e.g. MyClass$MyInnerClass
	 * @param access access of the new class
	 * @return a byte array, the new transformed class (useful to write the class into a file)
	 */

	public byte[] addInnerClass(String fullName, int access) {
		String outerClassName = fullName.split("\\$")[0];
		String simpleInnerClassName = fullName.split("\\$")[1];
		InnerClassNode newClass = new InnerClassNode(fullName, outerClassName, simpleInnerClassName, access);
		node.innerClasses.add(newClass);
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}
}
