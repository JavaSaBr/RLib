package rlib.compiler.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;

/**
 * Файловый менеджер по загрузке классов для компиляции.
 *
 * @author Ronn
 */
public class CompileJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    /**
     * Список имен загруженных классов.
     */
    private final Array<String> classNames;

    /**
     * Загрузчик скомпиленных классов.
     */
    private final CompileClassLoader loader;

    public CompileJavaFileManager(final StandardJavaFileManager fileManager, final CompileClassLoader loader) {
        super(fileManager);

        this.loader = loader;
        this.classNames = ArrayFactory.newArray(String.class);
    }

    /**
     * Очистка списка имен последних загруженных классов.
     */
    public void clear() {
        classNames.clear();
    }

    /**
     * @return список последних загруженны классов.
     */
    public String[] getClassNames() {
        return classNames.toArray(new String[classNames.size()]);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(final Location location, final String name, final Kind kind, final FileObject sibling) throws IOException {

        final CompileByteCode byteCode = new CompileByteCode(name);

        loader.addByteCode(byteCode);
        classNames.add(name);

        return byteCode;
    }
}
