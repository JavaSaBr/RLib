package rlib.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockIntegerDictionary}.
 *
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockIntegerDictionary<V> extends ConcurrentAtomicARSWLockIntegerDictionary<V> {

    public FinalConcurrentAtomicARSWLockIntegerDictionary() {
    }

    public FinalConcurrentAtomicARSWLockIntegerDictionary(float loadFactor) {
        super(loadFactor);
    }

    public FinalConcurrentAtomicARSWLockIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    public FinalConcurrentAtomicARSWLockIntegerDictionary(int initCapacity) {
        super(initCapacity);
    }
}
