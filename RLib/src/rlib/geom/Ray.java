package rlib.geom;

/**
 * Реализация модели луча.
 * 
 * @author Ronn
 */
public class Ray {

	/** стартовая точка луча */
	protected Vector start;
	/** точка направления луча */
	protected Vector direction;

	/**
	 * @return точка направления луча.
	 */
	public final Vector getDirection() {
		return direction;
	}

	/**
	 * @return start точка старта луча.
	 */
	public final Vector getStart() {
		return start;
	}

	/**
	 * @param direction точка направления луча.
	 */
	public final void setDirection(Vector direction) {
		this.direction = direction;
	}

	/**
	 * @param start точка старта луча.
	 */
	public final void setStart(Vector start) {
		this.start = start;
	}
}
