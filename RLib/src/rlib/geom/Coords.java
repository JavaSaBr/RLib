package rlib.geom;

import rlib.gamemodel.GameObject;
import rlib.logging.Loggers;
import rlib.util.Rnd;
import rlib.util.array.Array;

/**
 * Класс для рассчета координат.
 * 
 * @author Ronn
 */
public abstract class Coords {

	/**
	 * Генерация дуговых позиций.
	 * 
	 * @param type тип позиций.
	 * @param x целевая координата.
	 * @param y целевая координата.
	 * @param z целевая координата.
	 * @param heading разворот.
	 * @param radius радиус формирования.
	 * @param count кол-во необходимых позиций.
	 * @param degree положение на окружности центра дуги.
	 * @param width ширина дуги.
	 * @return массив позиций.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends GamePoint> T[] arcCoords(Class<T> type, float x, float y, float z, int heading, int radius, int count, int degree, int width) {

		T[] locs = (T[]) java.lang.reflect.Array.newInstance(type, count);

		float current = Angles.headingToDegree(heading) - degree;

		float min = current - width;
		float max = current + width;

		float angle = Math.abs(min - max) / count;

		for(int i = 0; i < count; i++) {
			try {

				T loc = type.newInstance();

				float radians = Angles.degreeToRadians(min + angle * i);

				float newX = calcX(x, radius, radians);
				float newY = calcY(y, radius, radians);

				loc.setXYZ(newX, newY, z);

				locs[i] = loc;

			} catch(InstantiationException | IllegalAccessException e) {
				Loggers.warning(type, e);
			}
		}

		return locs;
	}

	/**
	 * Генерация дуговых позиций.
	 * 
	 * @param type тип позиций.
	 * @param object объект, вокруг которого и формируются позиции.
	 * @param radius радиус формирования.
	 * @param count кол-во необходимых позиций.
	 * @param degree положение на окружности центра дуги.
	 * @param width ширина дуги.
	 * @return массив позиций.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends GamePoint> T[] arcCoords(Class<T> type, GameObject object, int radius, int count, int degree, int width) {

		T[] locs = (T[]) java.lang.reflect.Array.newInstance(type, count);

		float current = Angles.headingToDegree(object.getHeading()) - degree;

		float min = current - width;
		float max = current + width;

		float angle = Math.abs(min - max) / count;

		float x = object.getX();
		float y = object.getY();
		float z = object.getZ();

		for(int i = 0; i < count; i++) {
			try {

				T loc = type.newInstance();

				float radians = Angles.degreeToRadians(min + angle * i);

				float newX = calcX(x, radius, radians);
				float newY = calcY(y, radius, radians);

				loc.setXYZ(newX, newY, z);

				locs[i] = loc;

			} catch(InstantiationException | IllegalAccessException e) {
				Loggers.warning(type, e);
			}
		}

		return locs;
	}

	/**
	 * Рассчет х координаты с учетом дистанции и разворота.
	 * 
	 * @param x стартовая х координата.
	 * @param distance дистанция сдвига.
	 * @param radians направление сдвига.
	 * @return новая х координата.
	 */
	public static float calcX(float x, int distance, float radians) {
		return x + distance * (float) Math.cos(radians);
	}

	/**
	 * Рассчет х координаты с учетом дистанции и разворота.
	 * 
	 * @param x стартовая х координата.
	 * @param distance дистанция сдвига.
	 * @param heading направление сдвига.
	 * @return новая х координата.
	 */
	public static float calcX(float x, int distance, int heading) {
		return x + distance * (float) Math.cos(Angles.headingToRadians(heading));
	}

	/**
	 * Рассчет х координаты с учетом дистанции и разворота.
	 * 
	 * @param x стартовая х координата.
	 * @param distance дистанция сдвига.
	 * @param heading направление сдвига.
	 * @param offset смещение по градусам.
	 * @return новая х координата.
	 */
	public static float calcX(float x, int distance, int heading, int offset) {
		return x + distance * (float) Math.cos(Angles.headingToRadians(heading + offset));
	}

	/**
	 * Рассчет у координаты с учетом дистанции и разворота.
	 * 
	 * @param у стартовая у координата.
	 * @param distance дистанция сдвига.
	 * @param radians направление сдвига.
	 * @return новая у координата.
	 */
	public static float calcY(float y, int distance, float radians) {
		return y + distance * (float) Math.sin(radians);
	}

	/**
	 * Рассчет у координаты с учетом дистанции и разворота.
	 * 
	 * @param у стартовая у координата.
	 * @param distance дистанция сдвига.
	 * @param heading направление сдвига.
	 * @return новая у координата.
	 */
	public static float calcY(float y, int distance, int heading) {
		return y + distance * (float) Math.sin(Angles.headingToRadians(heading));
	}

	/**
	 * Рассчет у координаты с учетом дистанции и разворота.
	 * 
	 * @param у стартовая у координата.
	 * @param distance дистанция сдвига.
	 * @param heading направление сдвига.
	 * @param offset смещение по градусам.
	 * @return новая у координата.
	 */
	public static float calcY(float y, int distance, int heading, int offset) {
		return y + distance * (float) Math.sin(Angles.headingToRadians(heading + offset));
	}

	/**
	 * Генерация массива круговых позиций с одинаковым интервлаом.
	 * 
	 * @param type тип массива позиций.
	 * @param x центральная координата.
	 * @param y центральная координата.
	 * @param z центральная координата.
	 * @param radius радиус разброса.
	 * @param count кол-во позиций.
	 * @return массив позиций.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends GamePoint> T[] circularCoords(Class<T> type, float x, float y, float z, int radius, int count) {
		T[] locs = (T[]) java.lang.reflect.Array.newInstance(type, count);

		float angle = 360F / count;

		for(int i = 1; i <= count; i++) {
			try {

				T loc = type.newInstance();

				float radians = Angles.degreeToRadians(i * angle);

				float newX = calcX(x, radius, radians);
				float newY = calcY(y, radius, radians);

				loc.setXYZ(newX, newY, z);

				locs[i - 1] = loc;

			} catch(InstantiationException | IllegalAccessException e) {
				Loggers.warning(type, e);
			}
		}

		return locs;
	}

	/**
	 * Получение точек по кругу от указанной точки.
	 * 
	 * @param source массив исходных точек.
	 * @param x координата центра.
	 * @param y координата центра.
	 * @param z координата центра.
	 * @param count кол-во точек.
	 * @param radius радиус от центра.
	 * @return массив точек.
	 */
	public static <T extends GamePoint> T[] getCircularPoints(T[] source, float x, float y, float z, int count, int radius) {

		if(count < 1) {
			return source;
		}

		float angle = 360F / count;

		for(int i = 1; i <= count; i++) {

			float radians = Angles.degreeToRadians(angle * i);

			float newX = x + radius * (float) Math.cos(radians);
			float newY = y + radius * (float) Math.sin(radians);

			T point = source[i - 1];

			point.setXYZ(newX, newY, z);
		}

		return source;
	}

	/**
	 * Рассчет случайной точки.
	 * 
	 * @param x центральная координата.
	 * @param y центральная координата.
	 * @param z центральная координата.
	 * @param radiusMin минимальный радиус рандома.
	 * @param radiusMax максимальный радиус рандома.
	 * @return новая точка.
	 */
	public static <T extends GamePoint> T randomCoords(T loc, float x, float y, float z, int radiusMin, int radiusMax) {
		return randomCoords(loc, x, y, z, Rnd.nextInt(35000), radiusMin, radiusMax);
	}

	/**
	 * Рассчет случайной точки.
	 * 
	 * @param x центральная координата.
	 * @param y центральная координата.
	 * @param z центральная координата.
	 * @param heading направление объекта.
	 * @param radiusMin минимальный радиус рандома.
	 * @param radiusMax максимальный радиус рандома.
	 * @return новая точка.
	 */
	public static <T extends GamePoint> T randomCoords(T loc, float x, float y, float z, int heading, int radiusMin, int radiusMax) {

		if(radiusMax == 0 || radiusMax < radiusMin) {
			loc.setXYZH(x, y, z, heading);
			return loc;
		}

		int radius = Rnd.nextInt(radiusMin, radiusMax);
		float radians = Angles.degreeToRadians(Rnd.nextInt(0, 360));

		float newX = calcX(x, radius, radians);
		float newY = calcY(y, radius, radians);

		loc.setXYZH(newX, newY, z, heading);

		return loc;
	}

	/**
	 * Спавн объектов вокруг объекта.
	 * 
	 * @param locator центральный объект.
	 * @param objects список объектов, которые нужно отспавнить.
	 * @param radius радиус, в котором нужно отспавнить объекты.
	 */
	public static void spawnCircularObjects(GameObject locator, Array<? extends GameObject> objects, int radius) {

		if(objects.size() < 1) {
			return;
		}

		float angle = 360F / objects.size();

		float x = locator.getX();
		float y = locator.getY();
		float z = locator.getZ();

		for(int i = 1, length = objects.size(); i <= length; i++) {

			float radians = Angles.degreeToRadians(angle * i);

			float newX = x + radius * (float) Math.cos(radians);
			float newY = y + radius * (float) Math.sin(radians);

			GameObject item = objects.get(i - 1);

			item.spawnMe(newX, newY, z, 0);
		}
	}

	/**
	 * Спавн объектов вокруг объекта.
	 * 
	 * @param locator центральный объект.
	 * @param objects список объектов, которые нужно отспавнить.
	 * @param length кол-во объектов.
	 * @param radius радиус, в котором нужно отспавнить объекты.
	 */
	public static void spawnCircularObjects(GameObject locator, GameObject[] objects, int length, int radius) {

		if(length < 1) {
			return;
		}

		float angle = 360F / length;

		float x = locator.getX();
		float y = locator.getY();
		float z = locator.getZ();

		for(int i = 1; i <= length; i++) {

			float radians = Angles.degreeToRadians(angle * i);

			float newX = x + radius * (float) Math.cos(radians);
			float newY = y + radius * (float) Math.sin(radians);

			GameObject item = objects[i - 1];

			item.spawnMe(newX, newY, z, 0);
		}
	}
}
