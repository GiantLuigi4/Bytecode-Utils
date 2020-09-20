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
	private final ICompiler compiler;
	
	public Janino_Compiler(ICompiler compiler) {
		this.compiler = compiler;
	}
	
	public Janino_Compiler() {
		CompilerFactory compilerFactory = new CompilerFactory();
		this.compiler = compilerFactory.newCompiler();
		
		ResourceFinder finder = new ResourceFinder() {
			@Override
			public Resource findResource(String resourceName) {
				return new Resource() {
					@Override
					public InputStream open() {
						return new ByteArrayInputStream(new byte[0]);
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
		};
		this.compiler.setSourceFinder(finder);
	}
	
	public byte[] compile(String source, String name) throws IOException, CompileException {
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
	
	public byte[] compile(String source, String name, String[] otherClasses) throws IOException, CompileException {
		HashMap<String, byte[]> classes = new HashMap<>();
		compiler.setClassFileCreator(new MapResourceCreator(classes));
		
		Resource[] resources = buildResources(
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
				},
				otherClasses
		);
		
		compiler.compile(resources);
		
		return (byte[]) classes.values().toArray()[0];
	}
	
	private Resource[] buildResources(Resource resource, String[] otherClasses) {
		Resource[] resources = new Resource[otherClasses.length + 1];
		resources[0] = resource;
		for (int i = 0; i < otherClasses.length; i++) {
			int finalI = i;
			resources[i + 1] = new Resource() {
				@Override
				public InputStream open() {
					return Janino_Compiler.class.getClassLoader().getResourceAsStream(otherClasses[finalI]);
				}
				
				@Override
				public String getFileName() {
					return otherClasses[finalI];
				}
				
				@Override
				public long lastModified() {
					return new Date().getTime();
				}
			};
		}
		return resources;
	}
}
