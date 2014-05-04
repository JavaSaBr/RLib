package rlib.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import rlib.test.concurrent.lock.TestPrimitiveAtomicReadWriteLock;

@RunWith(Suite.class)
@SuiteClasses({ TestPrimitiveAtomicReadWriteLock.class
})
public class AllTests {

}
