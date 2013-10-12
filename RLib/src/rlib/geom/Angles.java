package rlib.geom;

/**
 * Класс для работы с углами.
 * 
 * @author Ronn
 */
public abstract class Angles {

	private static final float HEADINGS_IN_PI = 10430.378350470452724949566316381F;

	/**
	 * Расчет разворота в указанные координаты.
	 * 
	 * @param x начальная координата.
	 * @param y начальная координата.
	 * @param targetX целевая координата.
	 * @param targetY целевая координата.
	 * @return нужный разворот.
	 */
	public static int calcHeading(float x, float y, float targetX, float targetY) {
		return (int) (Math.atan2(y - targetY, x - targetX) * HEADINGS_IN_PI) + 32768;
	}

	/**
	 * Расчет относительного положения.
	 * 
	 * @param x начальная координата.
	 * @param y начальная координата.
	 * @param heading разворот.
	 * @param targetX целевая координата.
	 * @param targetY целевая координата.
	 * @return нужный разворот.
	 */
	public static int calcHeadingTo(float x, float y, int heading, float targetX, float targetY) {

		int newHeading = calcHeading(x, y, targetX, targetY);

		newHeading = heading - newHeading;

		if(newHeading < 0) {
			newHeading = newHeading + 1 + Integer.MAX_VALUE & 0xFFFF;
		} else if(newHeading > 0xFFFF) {
			newHeading &= 0xFFFF;
		}

		return newHeading;
	}

	/**
	 * Конвектирует градус в heading.
	 * 
	 * @param degree кол-во градусов.
	 * @return heading направление разворота.
	 */
	public static int degreeToHeading(float degree) {

		if(degree < 0) {
			degree += 360f;
		}

		return (int) (degree * 182.044444444f);
	}

	/**
	 * Конвектирование градусы в радианы.
	 * 
	 * @param angle кол-во градусов.
	 * @return кол-во радианов.
	 */
	public static float degreeToRadians(float angle) {
		return angle * 3.141592653f / 180f;
	}

	/**
	 * Получаем относительный градус между 2 точками.
	 * 
	 * @param startX х координата первой точки.
	 * @param startY у координата второй точки.
	 * @param endX х координата второй точки.
	 * @param endY у координата второй точки.
	 * @return кол-во градусов.
	 */
	public static float getAngleFrom(float startX, float startY, float endX, float endY) {

		float angle = (float) Math.toDegrees(Math.atan2(startY - endY, startX - endX));

		if(angle <= 0F) {
			angle += 360F;
		}

		return angle;
	}

	/**
	 * Конвектирование heading в градусы.
	 * 
	 * @param heading направление разворота.
	 * @return кол-во градусов.
	 */
	public static float headingToDegree(int heading) {

		float angle = heading / 182.044444444f;

		if(angle == 0) {
			angle = 360f;
		}

		return angle;
	}

	/**
	 * Конвектирование heading в радианы.
	 * 
	 * @param heading направление разворота.
	 * @return кол-во радианов.
	 */
	public static float headingToRadians(int heading) {

		float angle = heading / 182.044444444f;

		if(angle == 0) {
			angle = 360f;
		}

		return angle * 3.141592653f / 180f;
	}

	/**
	 * Рассчет вхождения в относительную область перед точкой точки.
	 * 
	 * @param x координата первой точки.
	 * @param y координата первой точки.
	 * @param heading направление области.
	 * @param targetX координата второй точки.
	 * @param targetY координата второй точки.
	 * @param width ширина области.
	 * @return входит ли.
	 */
	public static boolean isInDegree(float x, float y, int heading, float targetX, float targetY, int width) {

		int angle = (int) Angles.headingToDegree(calcHeadingTo(x, y, heading, targetX, targetY));
		int degree = (int) headingToDegree(heading);

		int min = degree - width;
		int max = degree + width;

		if(min < 0) {
			min += 360;
		}

		if(max < 0) {
			max += 360;
		}

		boolean flag = angle - degree > 180;

		if(flag) {
			angle -= 360;
		}

		if(angle > max) {
			return false;
		}

		angle += 360;

		return angle > min;
	}
}
