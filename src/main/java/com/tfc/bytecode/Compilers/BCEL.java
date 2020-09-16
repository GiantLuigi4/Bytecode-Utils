package com.tfc.bytecode.Compilers;

import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;

//TODO
public class BCEL {
	//http://commons.apache.org/proper/commons-bcel/manual/bcel-api.html
	public byte[] compile(String name, String superName, ArrayList<FieldNode> fields, ArrayList<MethodNodeSource> methods) throws CannotCompileException, IOException, NotFoundException {
//		ClassGen gen = new ClassGen(
//				name,superName,name.replace(".","/")+".java", Modifier.PUBLIC,null
//		);
//		MethodGen gen1 = new MethodGen(Modifier.PUBLIC, Type.getReturnType("java.lang.Object"),);
//		gen.addMethod();
		return null;
	}
}
