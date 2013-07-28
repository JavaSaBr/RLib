package rlib.compiler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Контейнер байткода класса в памяти.
 * 
 * @author Ronn
 */
public class CompileByteCode extends SimpleJavaFileObject implements ByteSource
{
	/** контейнер байткода класса в памяти */
	private final ByteArrayOutputStream outputStream;

	public CompileByteCode(String name)
	{
		super(URI.create("byte:///" + name.replace('/', '.') + Kind.CLASS.extension), Kind.CLASS);

		this.outputStream = new ByteArrayOutputStream();
	}

	@Override
	public OutputStream openOutputStream()
	{
		return outputStream;
	}

	@Override
	public byte[] getByteSource()
	{
		return outputStream.toByteArray();
	}
}
