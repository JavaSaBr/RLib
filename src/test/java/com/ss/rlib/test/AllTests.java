package com.ss.rlib.test;

import com.ss.rlib.test.concurrent.lock.TestPrimitiveAtomicReadWriteLock;
import com.ss.rlib.test.util.array.TestFastArray;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ss.rlib.test.concurrent.executor.TestThreadPoolTaskExecutor;

@RunWith(Suite.class)
@SuiteClasses({
        TestThreadPoolTaskExecutor.class,
        TestPrimitiveAtomicReadWriteLock.class,
        TestFastArray.class
})
public class AllTests {

}
