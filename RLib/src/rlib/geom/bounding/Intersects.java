package rlib.geom.bounding;

import static java.lang.Math.abs;

/**
 * @author Ronn
 */
public class Intersects {

	public static boolean intersects(float start, float dir, float min, float max, float enter, float exit) {

		if(abs(dir) < 1.0E-8) {
			return (start >= min && start <= max);
		}

		float ooDir = 1.0f / dir;
		float t0 = (min - start) * ooDir;
		float t1 = (max - start) * ooDir;

		if(t0 > t1) {
			float temp = t1;
			t1 = t0;
			t0 = temp;
		}

		if(t0 > exit || t1 < enter)
			return false;

		return true;
	}

	private Intersects() {
		throw new RuntimeException();
	}

}
