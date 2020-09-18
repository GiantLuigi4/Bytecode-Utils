package com.tfc.bytecode;

public enum EnumCompiler {
	JANINO("janino"),
	ASM("asm"),
	BCEL("bcel"),
	JAVASSIST("javassist"),
	;
	
	String name;
	
	EnumCompiler(String name) {
		this.name = name;
	}
}
