package com.tfc.bytecode;

import com.tfc.bytecode.Compilers.Javassist_Compiler;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Compiler {
	private static final Javassist_Compiler javassist = new Javassist_Compiler();
	
	public static byte[] compile(String name, String superName, ArrayList<FieldNode> fields, ArrayList<MethodNodeSource> methods) {
		try {
			Class.forName("javassist.compiler.Javac");
			return javassist.compile(name, superName, fields, methods);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		return null;
	}
	
	public static byte[] compile(String src) {
		try {
			Class.forName("javassist.compiler.Javac");
			return javassist.compile(src);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		return null;
	}
	
	public static byte[] compile(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		String src = new String(bytes);
		return compile(src);
	}
}
