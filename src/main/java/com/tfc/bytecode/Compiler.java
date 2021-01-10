package com.tfc.bytecode;

import com.tfc.bytecode.compilers.ASM_Compiler;
import com.tfc.bytecode.compilers.Janino_Compiler;
import com.tfc.bytecode.compilers.Javassist_Compiler;
import com.tfc.bytecode.utils.class_structure.ClassNode;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.FieldNodeSource;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Compiler {
	private static final Javassist_Compiler javassist = new Javassist_Compiler();
	private static final ASM_Compiler asm = new ASM_Compiler();
	private static final Janino_Compiler janino = new Janino_Compiler();
	
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
		try {
			Class.forName("org.objectweb.asm.ClassWriter");
			ArrayList<FieldNodeSource> fieldNodes = new ArrayList<>();
			for (FieldNode fNode : fields)
				new FieldNodeSource(fNode);
			ClassNode node = new ClassNode(name, superName, null, Modifier.PUBLIC, fieldNodes, methods, new ArrayList<>());
			return asm.compile(node);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		return null;
	}
	
	/**
	 * Compiles a class from a raw source code file
	 * order of fallbacks = janinio -> javassist -> fail
	 *
	 * @param src the source code
	 * @return the byte array of the class
	 */
	public static byte[] compile(String src) {
		try {
			Class.forName("javassist.compiler.Javac");
			return compile(EnumCompiler.JAVASSIST, src);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		try {
			Class.forName("org.codehaus.janino.CompilerFactory");
			return compile(EnumCompiler.JANINO, src);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		try {
			Class.forName("org.objectweb.asm.ClassWriter");
			return compile(EnumCompiler.ASM, src);
		} catch (ClassNotFoundException ignored) {
		} catch (Throwable err) {
			err.printStackTrace();
			throw new RuntimeException(err);
		}
		return null;
	}
	
	/**
	 * Compiles a class from an input stream
	 * {@link Compiler#compile(String)} for fallback order
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
	
	/**
	 * compile a class with a specific compiler
	 *
	 * @param compiler the compiler you want to use
	 * @param src      the code you want to compile
	 * @return the compiled class
	 */
	public static byte[] compile(EnumCompiler compiler, String src) {
		try {
			switch (compiler) {
				case ASM:
					return asm.compile(src);
				case BCEL:
					return null;
				case JANINO:
					return janino.compile(src, "a");
				case JAVASSIST:
					return javassist.compile(src);
			}
		} catch (Throwable err) {
			throw new RuntimeException(err);
		}
		return null;
	}
	
	public void setReferenceLoader(ClassLoader loader) {
		javassist.classLoaderReference = loader;
		janino.classLoaderReference = loader;
	}
}
