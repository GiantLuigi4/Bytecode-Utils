package com.github.lorenzopapi.asmutils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class InnerClassesUtils {

	static ClassReader reader = null;
	static ClassNode node = null;

	public static void main(String[] args) throws IOException {
		reader = new ClassReader("EmptyClass");
		node = new ClassNode();
		reader.accept(node, 0);
		addInnerClass("EmptyClass$NewInner", ACC_PRIVATE);
		byte[] bytes = changeInnerClassAccess(ACC_PUBLIC, "InnerClass");

		FileOutputStream stream = new FileOutputStream("yes.class");
		stream.write(bytes);
		stream.close();
	}

	public static byte[] changeInnerClassAccess(int newAccess, String className) {
		for (InnerClassNode innerClass : node.innerClasses) {
			if (innerClass.innerName.equals(className))
				innerClass.access = newAccess;
		}
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}

	public static byte[] addInnerClass(String fullName, int access) {
		String outerClassName = fullName.split("\\$")[0];
		String simpleInnerClassName = fullName.split("\\$")[1];
		InnerClassNode newClass = new InnerClassNode(fullName, outerClassName, simpleInnerClassName, access);
		node.innerClasses.add(newClass);
		ClassWriter result = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(result);
		return result.toByteArray();
	}
}
