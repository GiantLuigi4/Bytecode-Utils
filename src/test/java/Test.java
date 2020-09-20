import com.tfc.bytecode.Compiler;
import com.tfc.bytecode.asm.ASM.ASM;
import com.tfc.bytecode.compilers.ASM_Compiler;
import com.tfc.bytecode.compilers.Janino_Compiler;
import com.tfc.bytecode.loading.ForceLoad;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.InsnNode;
import com.tfc.bytecode.utils.class_structure.MethodNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;
import org.codehaus.commons.compiler.CompileException;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Test {
	public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, CompileException {
		FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir") + "\\test.class");
		FileOutputStream writer1 = new FileOutputStream(System.getProperty("user.dir") + "\\test.txt");
		FileOutputStream writer2 = new FileOutputStream(System.getProperty("user.dir") + "\\test2.class");
		FileOutputStream writer3 = new FileOutputStream(System.getProperty("user.dir") + "\\test2.txt");
		FileOutputStream writer4 = new FileOutputStream(System.getProperty("user.dir") + "\\test3.class");
		FileOutputStream writer5 = new FileOutputStream(System.getProperty("user.dir") + "\\test4.class");
		FileOutputStream writer6 = new FileOutputStream(System.getProperty("user.dir") + "\\test5.class");
		FileOutputStream writer7 = new FileOutputStream(System.getProperty("user.dir") + "\\test6.class");
		
		ArrayList<FieldNode> nodesF = new ArrayList<>();
		ArrayList<MethodNode> nodesM = new ArrayList<>();
		nodesF.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "hello", "int", null, 32));
		nodesF.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "hello1", "int", null, 32));
		MethodNode node = new MethodNode(new MethodNodeSource(
				"public int test(int var1, long var2) {" +
						"	return var1 + var2;" +
						"}"
		));
		MethodNode node1 = new MethodNode(new MethodNodeSource(
				"public long test(int var1, int var2) {" +
						"	return var1 + var2;" +
						"}"
		));
		
		MethodNode con = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		con.addInstruction(new InsnNode(InsnNode.InsnType.VAR_INSN, new Object[]{Opcodes.ALOAD, 0}));
		con.addInstruction(new InsnNode(InsnNode.InsnType.METHOD_INSN, new Object[]{Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false}));
		con.addInstruction(new InsnNode(InsnNode.InsnType.INSN, new Object[]{Opcodes.RETURN}));
		
		nodesM.add(node);
		nodesM.add(node1);
		nodesM.add(con);
		byte[] bytesASM = new ASM_Compiler().compile("hello32", Opcodes.ACC_PUBLIC, "", new String[0], nodesF, nodesM);
		writer.write(bytesASM);
		writer1.write(bytesASM);
		try {
			ForceLoad.forceLoad(ASM_Compiler.class.getClassLoader(), bytesASM);
			Class.forName("hello32").getMethod("test", int.class, int.class).invoke(Class.forName("hello32").newInstance(), 0, 4);
		} catch (Throwable err) {
			err.printStackTrace();
		}
		
		ArrayList<MethodNodeSource> nodesMS = new ArrayList<>(Arrays.asList(new MethodNodeSource(
				"public static void hello() {" +
						"	System.out.println(hello);" +
						"	System.out.println(hello1);" +
						"}"
		), new MethodNodeSource(
				"public static void hello1() {" +
						"	hello();" +
						"	System.out.println(\"hello1 has been called\");" +
						"}"
		)));
		byte[] bytes = Compiler.compile("hello", "", nodesF, nodesMS);
		byte[] bytes1 = Compiler.compile("package hello;class hello1 {public int i = 0;public static int i1 = 2;public static void hello(){System.out.println(\"hello\");}public static void hello1(){System.out.println(\"hello\");}}");
		byte[] bytes2 = Compiler.compile(Objects.requireNonNull(Test.class.getClassLoader().getResourceAsStream("test_input_stream.java")));
		assert bytes != null;
		writer2.write(bytes);
		writer3.write(bytes);
		assert bytes1 != null;
		writer4.write(bytes1);
		writer5.write(bytes2);
		
		byte[] bytes3 = new Janino_Compiler().compile("" +
				"package test;" +
				"import hello.hello.helloa;" +
				"import hello.hello.hellob;" +
				"" +
				"public class hello {" +
				"	private String hello1 = \"hi\";" +
				"	" +
				"	private static helloa test;" +
				"	private static hellob test;" +
				"	" +
				"	public static void test() {" +
				"		System.out.println(\"hello\");" +
				"	}" +
				"}", "a"
		);
		writer6.write(bytes3);
		ASM asm = new ASM(bytes3);
		writer7.write(asm
				.transformField("hello1", "public static")
				.addField("hello2", "public", "Ljava/lang/String;", "h")
				.toBytes()
		);
		
		writer.close();
		writer1.close();
		writer2.close();
		writer3.close();
		writer4.close();
		writer5.close();
		writer6.close();
		writer7.close();
		
		ForceLoad.forceLoad(Test.class.getClassLoader(), bytes);
		Class.forName("hello").getMethod("hello1").invoke(null);
	}
}
