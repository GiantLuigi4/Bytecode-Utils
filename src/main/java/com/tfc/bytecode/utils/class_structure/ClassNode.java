package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ClassNode {
	public boolean isInterface = false;
	public String name = "";
	public String superName = "";
	public String[] interfaces = new String[0];
	public int modifs = 0;
	public ArrayList<FieldNodeSource> fields = new ArrayList<>();
	public ArrayList<MethodNodeSource> methods = new ArrayList<>();
	public ArrayList<ConstructorNodeSource> constructors = new ArrayList<>();
	
	public ClassNode() {
	}
	
	public ClassNode(String name, String superName, String[] interfaces, int modifs, ArrayList<FieldNodeSource> fields, ArrayList<MethodNodeSource> methods, ArrayList<ConstructorNodeSource> constructors) {
		this.name = name;
		if (superName.equals("")) superName = "java.lang.Object";
		this.superName = superName;
		this.interfaces = interfaces;
		this.modifs = modifs;
		this.fields = fields;
		this.methods = methods;
		this.constructors = constructors;
		this.isInterface = Modifier.isInterface(modifs);
	}
	
	public ClassNode(String name, String superName, String[] interfaces, String modifs, ArrayList<FieldNodeSource> fields, ArrayList<MethodNodeSource> methods, ArrayList<ConstructorNodeSource> constructors) {
		this.name = name;
		if (superName.equals("")) superName = "java.lang.Object";
		this.superName = superName;
		this.interfaces = interfaces;
		this.modifs = Access.parseAccess(modifs);
		this.fields = fields;
		this.methods = methods;
		this.constructors = constructors;
		this.isInterface = modifs.contains("interface ");
	}
}
