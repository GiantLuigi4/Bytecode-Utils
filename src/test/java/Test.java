import com.tfc.bytecode.Compiler;
import com.tfc.bytecode.compilers.ASM_Compiler;
import com.tfc.bytecode.loading.ForceLoad;
import com.tfc.bytecode.utils.class_structure.FieldNode;
import com.tfc.bytecode.utils.class_structure.MethodNode;
import com.tfc.bytecode.utils.class_structure.MethodNodeSource;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.compiler.CompileError;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Test {
	public static void main(String[] args) throws IOException, CannotCompileException, CompileError, NotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
		FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir") + "\\test.class");
		FileOutputStream writer1 = new FileOutputStream(System.getProperty("user.dir") + "\\test.txt");
		FileOutputStream writer2 = new FileOutputStream(System.getProperty("user.dir") + "\\test2.class");
		FileOutputStream writer3 = new FileOutputStream(System.getProperty("user.dir") + "\\test2.txt");
		FileOutputStream writer4 = new FileOutputStream(System.getProperty("user.dir") + "\\test3.class");
		FileOutputStream writer5 = new FileOutputStream(System.getProperty("user.dir") + "\\test4.class");
		
		ArrayList<FieldNode> nodesF = new ArrayList<>();
		ArrayList<MethodNode> nodesM = new ArrayList<>();
		nodesF.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "hello", "int", null, 32));
		nodesF.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "hello1", "int", null, 32));
//		nodesM.add(new MethodNode(Opcodes.ACC_PUBLIC,"hello","","none", new String[0]));
		writer.write(new ASM_Compiler().generate("hello", Opcodes.ACC_PUBLIC, "", new String[0], nodesF, nodesM));
		writer1.write(new ASM_Compiler().generate("hello", Opcodes.ACC_PUBLIC, "", new String[0], nodesF, nodesM));
		
		ArrayList<MethodNodeSource> nodesMS = new ArrayList<>();
		nodesMS.addAll(Arrays.asList(new MethodNodeSource(
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
		
		writer.close();
		writer1.close();
		writer2.close();
		writer3.close();
		writer4.close();
		writer5.close();
		
		ForceLoad.forceLoad(Test.class.getClassLoader(), bytes);
		Class.forName("hello").getMethod("hello1").invoke(null);
	}
}
