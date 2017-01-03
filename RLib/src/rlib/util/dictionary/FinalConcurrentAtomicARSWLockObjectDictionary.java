package rlib.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockObjectDictionary}.
 *
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockObjectDictionary<K, V> extends ConcurrentAtomicARSWLockObjectDictionary<K, V> {

    public FinalConcurrentAtomicARSWLockObjectDictionary() {
    }

    public FinalConcurrentAtomicARSWLockObjectDictionary(final float loadFactor) {
        super(loadFactor);
    }

    public FinalConcurrentAtomicARSWLockObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    public FinalConcurrentAtomicARSWLockObjectDictionary(final int initCapacity) {
        super(initCapacity);
    }
}
