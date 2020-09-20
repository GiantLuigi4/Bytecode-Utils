package com.tfc.bytecode.compilers;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.ICompiler;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.ResourceFinder;
import org.codehaus.janino.CompilerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

public class Janino_Compiler {
	public byte[] compile(String source, String name) throws IOException, CompileException {
		CompilerFactory compilerFactory = new CompilerFactory();
		ICompiler compiler = compilerFactory.newCompiler();
		compiler.setClassFileFinder(new ResourceFinder() {
			@Override
			public Resource findResource(String resourceName) {
				return new Resource() {
					@Override
					public InputStream open() throws IOException {
						return Janino_Compiler.class.getClassLoader().getResourceAsStream(resourceName);
					}
					
					@Override
					public String getFileName() {
						return resourceName;
					}
					
					@Override
					public long lastModified() {
						return new Date().getTime();
					}
				};
			}
		});
		
		HashMap<String, byte[]> classes = new HashMap<>();
		compiler.setClassFileCreator(new MapResourceCreator(classes));
		
		compiler.compile(new Resource[]{
				new Resource() {
					@Override
					public InputStream open() {
						return new ByteArrayInputStream(source.getBytes());
					}
					
					@Override
					public String getFileName() {
						return name;
					}
					
					@Override
					public long lastModified() {
						return new Date().getTime();
					}
				}
		});
		
		return (byte[]) classes.values().toArray()[0];
	}
}
