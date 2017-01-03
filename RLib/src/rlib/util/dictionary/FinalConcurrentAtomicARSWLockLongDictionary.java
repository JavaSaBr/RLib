package rlib.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockLongDictionary}.
 *
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockLongDictionary<V> extends ConcurrentAtomicARSWLockLongDictionary<V> {

    public FinalConcurrentAtomicARSWLockLongDictionary() {
    }

    public FinalConcurrentAtomicARSWLockLongDictionary(float loadFactor) {
        super(loadFactor);
    }

    public FinalConcurrentAtomicARSWLockLongDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    public FinalConcurrentAtomicARSWLockLongDictionary(int initCapacity) {
        super(initCapacity);
    }
}
