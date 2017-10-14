package grapher.fc;

// runtime creation and instanciation of class implementing Function interface
// shamelessly stolen from:
// https://javajazzle.wordpress.com/2011/06/29/dynamic-in-memory-compilation-using-javax-tools/


/* imports *****************************************************************/

import java.util.ArrayList;
import java.util.List;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.security.SecureClassLoader;

import java.net.URI;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileManager;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


/* factory *****************************************************************/

public class FunctionFactory {
	public static Function createFunction(String expression) {
		String name = "grapher.fc.DynamicFunction";
		
		StringBuilder src = new StringBuilder();
		src.append("package grapher.fc;\n");
		src.append("import static java.lang.Math.*;\n");
		src.append("public class DynamicFunction implements Function {\n");
		src.append("	public String toString() { return \"" + expression + "\"; }\n");
		src.append("	public double y(double x) { return " + expression + "; }\n");
		src.append("}\n");

		class CharSequenceJavaFileObject extends SimpleJavaFileObject {
			private CharSequence content;
			public CharSequenceJavaFileObject(String name, CharSequence content) {
				super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
				this.content = content;
			}
			public CharSequence getCharContent(boolean ignoreEncodingErrors) { return content; }
		}

		class JavaClassObject extends SimpleJavaFileObject {
			protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			public JavaClassObject(String name, Kind kind) {
				super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
			}
			public byte[] getBytes() { return bos.toByteArray(); }
			public OutputStream openOutputStream() throws IOException { return bos; }
		}
		
		class ClassFileManager extends ForwardingJavaFileManager {
			private JavaClassObject object;
			public ClassFileManager(StandardJavaFileManager manager) { super(manager); }
			public ClassLoader getClassLoader(Location location) {
				return new SecureClassLoader() {
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						byte[] b = object.getBytes();
						return super.defineClass(name, b, 0, b.length);
					}
				};
			}
			public JavaFileObject getJavaFileForOutput(Location location, String name, Kind kind, FileObject sibling) throws IOException {
				object = new JavaClassObject(name, kind);
				return object;
			}
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaFileManager file_manager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));
		List<JavaFileObject> files = new ArrayList<JavaFileObject>();
		files.add(new CharSequenceJavaFileObject(name, src.toString()));
		compiler.getTask(null, file_manager, null, null, null, files).call();
		
		try {
			return (Function)file_manager.getClassLoader(null).loadClass(name).newInstance();
		
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (NullPointerException e) {
		}

		throw new RuntimeException("unable to load class");
	}
}
