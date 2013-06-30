/**
 * 
 */
package rlib.geom;

/**
 * @author ronn
 *
 */
public class Ray
{
	protected Vector start;
	
	protected Vector direction;

	/**
	 * @return direction
	 */
	public final Vector getDirection()
	{
		return direction;
	}

	/**
	 * @return start
	 */
	public final Vector getStart()
	{
		return start;
	}

	/**
	 * @param direction задаваемое direction
	 */
	public final void setDirection(Vector direction)
	{
		this.direction = direction;
	}

	/**
	 * @param start задаваемое start
	 */
	public final void setStart(Vector start)
	{
		this.start = start;
	}
	
	
}
