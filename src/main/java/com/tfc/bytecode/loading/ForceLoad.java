//package com.tfc.bytecode.loading;
//
//import sun.reflect.CallerSensitive;
//import sun.reflect.Reflection;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//public class ForceLoad {
//	private static final Method m;
//
//	static {
//		try {
//			m = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * Forces a class loader to load a class
//	 *
//	 * @param loader the class loader to force to load the class
//	 * @param bytes  the bytes of the class that you want to load
//	 * @return the class
//	 * @throws InvocationTargetException if reflection fails
//	 * @throws IllegalAccessException    if reflection fails
//	 */
//	public static Class<?> forceLoad(ClassLoader loader, byte[] bytes) throws InvocationTargetException, IllegalAccessException {
//		m.setAccessible(true);
//		return (Class<?>) m.invoke(loader, bytes, 0, bytes.length);
//	}
//
//	/**
//	 * I don't think this works
//	 *
//	 * @param bytes the bytes of the class to load
//	 * @return the class
//	 * @throws InvocationTargetException
//	 * @throws IllegalAccessException
//	 */
//	@CallerSensitive
//	public static Class<?> forceLoad(byte[] bytes) throws InvocationTargetException, IllegalAccessException {
//		Class<?> caller = Reflection.getCallerClass();
//		m.setAccessible(true);
//		return (Class<?>) m.invoke(caller.getClassLoader(), bytes, 0, bytes.length);
//	}
//}
