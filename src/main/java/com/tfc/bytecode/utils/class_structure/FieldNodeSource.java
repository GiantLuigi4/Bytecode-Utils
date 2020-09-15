package com.tfc.bytecode.utils.class_structure;

import com.tfc.bytecode.utils.Access;

public class FieldNodeSource {
	public final String code;
	
	public FieldNodeSource(String code) {
		this.code = code;
	}
	
	public FieldNodeSource(FieldNode node) {
		String thisCode = Access.parseAccess(node.access);
		thisCode += (node.desc.replace("/", ".")) + " ";
		thisCode += node.name;
		thisCode += " = ";
		thisCode += node.value + ";";
		System.out.println(thisCode);
		code = thisCode;
	}
	
	public String getType() {
		String type = code;
		for (String modif : Access.modifs)
			type = type.replace(modif, "");
		type = (type.trim());
		System.out.println(code);
		return type.substring(0, type.indexOf(" "));
	}
	
	public String getName() {
		String name = code;
		for (String modif : Access.modifs)
			name = name.replace(modif, "");
		name = name.replace(getType() + " ", "");
		name = name.trim();
		if (name.contains(" "))
			return name.substring(0, name.indexOf(" "));
		else
			return name.substring(0, name.indexOf(";"));
	}
}
