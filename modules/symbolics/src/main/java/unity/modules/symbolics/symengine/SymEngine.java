package unity.modules.symbolics.symengine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.SystemUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public interface SymEngine extends Library
{
    int basic_add(Pointer s, Pointer a, Pointer b);

    int basic_diff(Pointer s, Pointer expr, Pointer sym);

    int basic_div(Pointer s, Pointer a, Pointer b);

    int basic_expand(Pointer s, Pointer a);

    void basic_free_heap(Pointer s);

    int basic_mul(Pointer s, Pointer a, Pointer b);

    Pointer basic_new_heap();

    int basic_pow(Pointer s, Pointer a, Pointer b);

    Pointer basic_str(Pointer s);

    void basic_str_free(Pointer s);

    Pointer basic_str_julia(Pointer s);

    int basic_sub(Pointer s, Pointer a, Pointer b);

    int function_symbol_set(Pointer s, String c, Pointer arg);

    int integer_set_si(Pointer s, long i);

    int real_double_set_d(Pointer s, double d);

    int symbol_set(Pointer s, String c);

    void vecbasic_free(Pointer self);

    Pointer vecbasic_new();

    int vecbasic_push_back(Pointer self, Pointer value);

    SymEngine INSTANCE = SymEngineLibrary.load();

    public static void main(String[] a)
    {
        final SymEngine sym = SymEngine.INSTANCE;

        final Pointer y = sym.basic_new_heap();
        sym.symbol_set(y, "y");

        final Pointer args = sym.vecbasic_new();
        sym.vecbasic_push_back(args, y);

        final Pointer x = sym.basic_new_heap();
        sym.function_symbol_set(x, "x", args);

        final Pointer z = sym.basic_new_heap();
        sym.basic_add(z, x, y);
        final Pointer zz = sym.basic_new_heap();
        sym.basic_mul(zz, z, y);
        final Pointer zz1 = sym.basic_new_heap();
        sym.basic_expand(zz1, zz);

        final Pointer str = sym.basic_str(zz1);
        System.out.println(str.getString(0));
        sym.basic_str_free(str);
        sym.basic_free_heap(x);
        sym.basic_free_heap(y);
        sym.basic_free_heap(z);
        sym.basic_free_heap(zz);
        sym.basic_free_heap(zz1);
        sym.vecbasic_free(args);
    }

    class SymEngineLibrary
    {
        static {
            try {
                final String path = SymEngine.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                        .getPath();
                if (path.endsWith("classes/")) {
                    final File location = new File(path).getParentFile().getParentFile().getParentFile()
                            .getParentFile();
                    if (SystemUtils.IS_OS_WINDOWS)
                        switch (SystemUtils.OS_ARCH) {
                        case "amd64":
                        case "x86_64":
                            loadNativeLibraries(new File(location, "target/windows-msvc-x86_64"));
                            break;
                        }
                    else if (SystemUtils.IS_OS_LINUX)
                        switch (SystemUtils.OS_ARCH) {
                        case "amd64":
                        case "x86_64":
                            loadNativeLibraries(new File(location, "target/linux-gcc-x86_64"));
                            break;
                        }
                } else {
                    final File location = new File(path).getParentFile().getParentFile();
                    loadNativeLibraries(location);
                }

            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        static SymEngine load()
        {
            return Native.loadLibrary(SymEngine.class);
        }

        /**
         * @param path
         */
        @SuppressWarnings("unchecked")
        static void loadNativeLibraries(File path)
        {
            try {
                final Field loadedLibraryNamesField = ClassLoader.class.getDeclaredField("loadedLibraryNames");
                loadedLibraryNamesField.setAccessible(true);
                final Set<String> loadedLibraryNames = new HashSet<>(
                        (Vector<String>) loadedLibraryNamesField.get(SymEngine.class.getClassLoader()));
                String[] libraryNames = new String[] {};
                if (SystemUtils.IS_OS_WINDOWS)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        path = new File(path, "bin");
                        libraryNames = new String[] { "mpir.dll", "symengine.dll" };
                        break;
                    }
                else if (SystemUtils.IS_OS_LINUX)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        path = new File(path, "lib");
                        libraryNames = new String[] { "symengine.so" };
                        break;
                    }
                for (final String libraryName : libraryNames) {
                    final String library = new File(path, libraryName).getAbsolutePath();
                    if (!loadedLibraryNames.contains(library))
                        System.load(library);
                }
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
