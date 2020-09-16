package com.tfc.bytecode;

import com.tfc.bytecode.compilers.Javassist_Compiler;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Compiler {
	private static final Javassist_Compiler javassist = new Javassist_Compiler();
	
	/**
	 * Compiles a class from a pre-parsed format
	 * order of fallbacks = javassist -> fail
	 *
	 * @param name      the name of the class
	 * @param superName the name of the class that the class extends from
	 * @param fields    the fields of the class
	 * @param methods   the methods of the class
	 * @return the byte array of the class
	 */
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
	
	/**
	 * Compiles a class from a raw source code file
	 * order of fallbacks = javassist -> fail
	 *
	 * @param src the source code
	 * @return the byte array of the class
	 */
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
	
	/**
	 * Compiles a class from an input stream
	 * order of fallbacks = javassist -> fail
	 *
	 * @param stream the stream to pull the source code from
	 * @return the byte array of the class
	 */
	public static byte[] compile(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		String src = new String(bytes);
		return compile(src);
	}
}
