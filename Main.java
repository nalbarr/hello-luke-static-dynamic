import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Main {
	// static method
	public static void foo() {
		System.out.println(String.format("dynamic foo()"));
	}

	// inner class
	/*
	// NAA
	// - Moved this to separate class outside of Main.java
	static class Foo {
		public int x;
		Foo(int x) {
			this.x = x;
		}
		void bar(int x) {
			this.x = x;
			System.out.println(String.format("Foo.bar(); x: %d", this.x));
		}
	}
	*/

	public static void explicitTypes() {
		int i = 1;
		String s = "foo";

		System.out.println(String.format("i: %d, s: %s", i, s));
	}

	public static void implicitTypes() {
		var i = 1;
		var s = "foo";

		System.out.println(String.format("i: %d, s: %s", i, s));
	}

	public static void methodInvocationReflection() {
		Class classesLoaded = null;
		Foo foo = null;
		try {
			classesLoaded = Class.forName("Foo");
			foo = (Foo) classesLoaded.getDeclaredConstructor().newInstance();
			// call method on reflective instance
			foo.bar();
		} catch (Throwable e) {
			// Will catch:
			// - InstantiationException
			// - IllegalAccessException
			// - ClassNotFoundException
			// ...			
			System.err.println(String.format("Caught exception: %s", e.getMessage()));
		}
	}

	public static void dynamicTypes() {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();

			// dynamic lookup of static method
			MethodHandle mh = lookup.findStatic(Main.class, "foo",
                	MethodType.methodType(void.class));
			mh.invokeExact();

			// dynamic lookup of class getter
			Foo foo = new Foo();
			MethodHandle mh2 = lookup.findVirtual(Foo.class, "bar", 
				MethodType.methodType(void.class));
			mh2.invoke(foo);

			// NAA
			// - note the need to runtime cast of a dynamic type
      		MethodHandle mh3 = lookup.findGetter(Foo.class, "x", int.class);
			int x = (int) mh3.invoke(foo);
			System.out.printf(String.format("x: %d", x));
			assert x == 222;
			  
		} catch (Throwable e) {
			// Will catch:
			// - IllegalAccessException
			// - NoSuchMethodException
			System.err.println(String.format("Caught exception: %s", e.getMessage()));
		} 
	}

	public static void main(String args[]) {
		explicitTypes();
		implicitTypes();
		methodInvocationReflection();
		dynamicTypes();
	}
}
