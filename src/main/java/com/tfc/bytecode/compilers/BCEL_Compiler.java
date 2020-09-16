package com.tfc.bytecode.compilers;

import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;

import java.util.ArrayList;

//import org.apache.bcel.generic.ClassGen;
//import org.apache.bcel.generic.MethodGen;
//import org.apache.bcel.generic.Type;

//TODO
public class BCEL_Compiler {
	//http://commons.apache.org/proper/commons-bcel/manual/bcel-api.html
	public byte[] compile(String name, String superName, ArrayList<FieldNode> fields, ArrayList<MethodNodeSource> methods) {
//		ClassGen gen = new ClassGen(
//				name,superName,name.replace(".","/")+".java", Modifier.PUBLIC, null
//		);
//		MethodGen gen1 = new MethodGen(Modifier.PUBLIC, Type.getReturnType("java.lang.Object"),);
//		gen.addMethod();
		return null;
	}
}
