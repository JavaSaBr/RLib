package rlib.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rlib.test.concurrent.lock.TestPrimitiveAtomicReadWriteLock;
import rlib.test.util.array.TestFastArray;

@RunWith(Suite.class)
@SuiteClasses({
	TestPrimitiveAtomicReadWriteLock.class,
	TestFastArray.class
})
public class AllTests {

}
