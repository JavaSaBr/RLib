package rlib.geom;

/**
 * Контейнер локальных объектов для векторных вычислений.
 * 
 * @author Ronn
 */
public final class VectorBuffer {

	public static VectorBuffer newInstance(int size) {
		return new VectorBuffer(size);
	}

	/** список локальных векторов */
	private final Vector[] vectors;

	/** индекс след. свободного вектора */
	private int vectorIndex;

	private VectorBuffer(int size) {
		this.vectors = new Vector[size];

		for(int i = 0, length = vectors.length; i < length; i++) {
			vectors[i] = Vector.newInstance();
		}
	}

	/**
	 * @return след. свободный вектор.
	 */
	public Vector getNextVector() {

		if(vectorIndex == vectors.length) {
			vectorIndex = 0;
		}

		return vectors[vectorIndex++];
	}
}
