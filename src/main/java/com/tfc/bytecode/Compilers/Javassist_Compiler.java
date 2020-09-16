package com.tfc.bytecode.Compilers;

import com.tfc.bytecode.utils.Formatter;
import com.tfc.bytecode.utils.Parser;
import com.tfc.bytecode.utils.class_structure.*;
import javassist.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Javassist_Compiler {
	private final ClassPool pool = ClassPool.getDefault();
	
	public Javassist_Compiler() {
		this.addClassPath(new ClassPath() {
			@Override
			public InputStream openClassfile(String classname) throws NotFoundException {
				return Javassist_Compiler.class.getClassLoader().getResourceAsStream(classname.replace(".", "/") + ".class");
			}
			
			@Override
			public URL find(String classname) {
				return Javassist_Compiler.class.getClassLoader().getResource(classname.replace(".", "/") + ".class");
			}
		});
	}
	
	public byte[] compile(String name, String superName, ArrayList<FieldNode> fields, ArrayList<MethodNodeSource> methods) throws CannotCompileException, IOException, NotFoundException {
		CtClass cc = pool.makeClass(name);
		if (superName.equals("")) superName = "java.lang.Object";
		cc.setSuperclass(pool.getCtClass(superName));
		for (FieldNode node : fields)
			cc.addField(CtField.make(new FieldNodeSource(node).code, cc));
		for (MethodNodeSource node : methods)
			cc.addMethod(CtNewMethod.make(node.code, cc));
		return cc.toBytecode();
	}
	
	//Object must be a string, I just had to do it this way because elsewise the signatures would be the same
	public byte[] compile(Object name, String superName, ArrayList<FieldNodeSource> fields, ArrayList<MethodNodeSource> methods) throws CannotCompileException, IOException, NotFoundException {
		if (!(name instanceof String))
			throw new RuntimeException(new IllegalArgumentException("Name should be an instance of a string."));
		CtClass cc = pool.makeClass((String) name);
		if (superName.equals("")) superName = "java.lang.Object";
		cc.setSuperclass(pool.getCtClass(superName));
		for (FieldNodeSource node : fields)
			cc.addField(CtField.make(node.code, cc));
		for (MethodNodeSource node : methods)
			cc.addMethod(CtNewMethod.make(node.code, cc));
		return cc.toBytecode();
	}
	
	public byte[] compile(ClassNode classNode) throws CannotCompileException, IOException, NotFoundException {
		CtClass cc;
		if (classNode.isInterface) cc = pool.makeInterface(classNode.name);
		else cc = pool.makeClass(classNode.name);
		cc.setModifiers(classNode.modifs);
		cc.setSuperclass(pool.getCtClass(classNode.superName));
		for (String interf : classNode.interfaces)
			cc.addInterface(pool.getCtClass(interf));
		for (FieldNodeSource node : classNode.fields)
			try {
				cc.addField(CtField.make(node.code, cc));
			} catch (Throwable err) {
				CtClass ctClass = null;
				String type = node.getType();
				if (type.equals(classNode.name))
					ctClass = cc;
				else
					pool.get(node.getType());
				assert ctClass != null;
				cc.addField(new CtField(ctClass, node.getName(), cc));
			}
		for (MethodNodeSource node : classNode.methods)
			cc.addMethod(CtNewMethod.make(node.code, cc));
		for (ConstructorNodeSource node : classNode.constructors)
			cc.addConstructor(CtNewConstructor.make(node.code, cc));
		return cc.toBytecode();
	}
	
	//T0DO: create a full runtime compiler using javassist
	//Whoops, I did it already
	public byte[] compile(String src) throws NotFoundException, CannotCompileException, IOException {
		src = Formatter.formatForCompile(src);
		ClassNode node = Parser.parse(src);
		return compile(node);
	}
	
	public void addClassPath(String path) throws NotFoundException {
		pool.appendClassPath(path);
	}
	
	public void addClassPath(ClassPath path) {
		pool.appendClassPath(path);
	}
}
