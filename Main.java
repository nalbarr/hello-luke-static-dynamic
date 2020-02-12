import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Main {
	// static method
	public static void foo() {
		System.out.println(String.format("dynamic foo()"));
	}

	// inner class
	static class Foo {
		int x;
		Foo(int x) {
			this.x = x;
		}
		void bar(int x) {
			this.x = x;
			System.out.println(String.format("x: %d", this.x));
		}
	}

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

	public static void dynamicTypes() {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();

			// dynamic lookup of static method
			MethodHandle mh = lookup.findStatic(Main.class, "foo",
                	MethodType.methodType(void.class));
			mh.invokeExact();

			// dynamic lookup of class getter
			/*
			Foo foo = new Foo(123);
			MethodHandle mh2 = lookup.findStatic(Foo.class, "x", int.class);
			mh2.invoke(bar, 345);

			// NAA
			// - note the need to runtime cast of a dynamic type
      			MethodHandle mh3 = lookup.findGetter(Main.Foo.class, "_x", int.class);
			int x = (int) mh3.invoke(foo);
      			System.out.printf(String.format("x: %d", x));
			*/
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
		dynamicTypes();
	}
}
