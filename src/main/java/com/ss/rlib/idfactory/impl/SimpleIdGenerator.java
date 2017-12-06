package com.ss.rlib.idfactory.impl;

import com.ss.rlib.idfactory.IdGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The simple implementation of ID generator.
 *
 * @author JavaSaBr
 */
public final class SimpleIdGenerator implements IdGenerator {

    /**
     * The IDs range.
     */
    private final int start;
    private final int end;

    /**
     * The next id.
     */
    private final AtomicInteger nextId;

    public SimpleIdGenerator(final int start, final int end) {
        this.start = start;
        this.end = end;
        this.nextId = new AtomicInteger(start);
    }

    @Override
    public int getNextId() {
        nextId.compareAndSet(end, start);
        return nextId.incrementAndGet();
    }

    @Override
    public int usedIds() {
        return nextId.get() - start;
    }
}
