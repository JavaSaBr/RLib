package rlib.classpath;

import rlib.util.array.Array;
import rlib.util.array.Arrays;
import rlib.util.linkedlist.AbstractLinkedList;

/**
 * Фабрика сканеров classpath.
 * 
 * @author Ronn
 */
public final class ClassPathScanners
{
	private ClassPathScanners()
	{
		throw new RuntimeException();
	}

	public static final ClassPathScaner newDefaultScanner()
	{
		return new ClassPathScanerImpl();
	}

	public static void main(String[] args)
	{
		ClassPathScaner scanner = newDefaultScanner();
		scanner.scanning();

		Array<Class<AbstractLinkedList<?>>> classes = Arrays.toArray(Class.class);

		scanner.findInherited(classes, AbstractLinkedList.class);

		System.out.println(classes);
	}
}
