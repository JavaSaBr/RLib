package javasabr.rlib.common.geom;

import static javasabr.rlib.common.geom.Vector3f.substract;

import javasabr.rlib.common.util.ExtMath;
import org.jspecify.annotations.NullMarked;

/**
 * Geometry 3D plane.<br> Follow to the formula: <pre>Ax + By + Cz + D = 0</pre>
 *
 * @author zcxv
 */
@NullMarked
public class Plane {

  /**
   * The plane normal
   */
  private final Vector3f normal;

  /**
   * The D component, inverted by sign.
   */
  private float d;

  public Plane(Vector3f first, Vector3f second, Vector3f third) {

    var ba = substract(second, first);
    var ca = substract(third, first);

    normal = ba
        .cross(ca)
        .normalizeLocal();

    this.d = first.dot(normal);
  }

  public Plane(Vector3f planePoint, Vector3f normal) {
    this.normal = normal.clone();
    this.d = planePoint.dot(normal);
  }

  /**
   * Return a plane normal.
   *
   * @return normal.
   */
  public Vector3f getNormal() {
    return normal;
  }

  /**
   * Return a D component inverted by sign.
   *
   * @return the D component.
   */
  public float getD() {
    return d;
  }

  /**
   * Multiply plane by scalar.
   *
   * @param scalar scalar.
   * @return this plane.
   */
  public Plane multLocal(float scalar) {
    normal.multLocal(scalar);
    d *= scalar;
    return this;
  }

  /**
   * Divide plane by scalar.
   *
   * @param scalar scalar.
   * @return this plane .
   */
  public Plane divideLocal(float scalar) {
    normal.divideLocal(scalar);
    d /= scalar;
    return this;
  }

  /**
   * Add values to plane.
   *
   * @param x the X axis value.
   * @param y the Y axis value.
   * @param z the Z axis value.
   * @param d the D component.
   * @return this plane .
   */
  public Plane addLocal(float x, float y, float z, float d) {
    normal.addLocal(x, y, z);
    this.d += d;
    return this;
  }

  /**
   * Subtract values to plane.
   *
   * @param x the X axis value.
   * @param y the Y axis value.
   * @param z the Z axis value.
   * @param d the D component.
   * @return this plane.
   */
  public Plane subtractLocal(float x, float y, float z, float d) {
    normal.subtractLocal(x, y, z);
    this.d -= d;
    return this;
  }

  /**
   * Multiply plane by other plane.
   *
   * @param plane the other plane.
   * @return this plane .
   */
  public Plane multLocal(Plane plane) {
    normal.multLocal(plane.normal);
    d *= plane.d;
    return this;
  }

  /**
   * Divide plane by other plane.
   *
   * @param plane the other plane.
   * @return this plane .
   */
  public Plane divideLocal(Plane plane) {
    normal.divideLocal(plane.normal);
    d /= plane.d;
    return this;
  }

  /**
   * Add plane by other plane.
   *
   * @param plane the other plane.
   * @return this plane
   */
  public Plane addLocal(Plane plane) {
    normal.addLocal(plane.normal);
    d += plane.d;
    return this;
  }

  /**
   * Subtract plane by other plane.
   *
   * @param plane the other plane
   * @return this plane
   */
  public Plane subtractLocal(Plane plane) {
    normal.subtractLocal(plane.normal);
    d -= plane.d;
    return this;
  }

  /**
   * Multiply plane by vector.<br> Its operation is equals to multiply plane normal vector with vector.
   *
   * @param vector the vector.
   * @return this plane.
   */
  public Plane multLocal(Vector3f vector) {
    normal.multLocal(vector);
    return this;
  }

  /**
   * Divide plane by vector.<br> Its operation is equals to divide plane normal vector with vector.
   *
   * @param vector the vector.
   * @return this plane .
   */
  public Plane divideLocal(Vector3f vector) {
    normal.divideLocal(vector);
    return this;
  }

  /**
   * Add plane by vector.<br> Its operation is equals to: plane normal plus vector.
   *
   * @param vector the vector.
   * @return this plane.
   */
  public Plane addLocal(Vector3f vector) {
    normal.addLocal(vector);
    return this;
  }

  /**
   * Subtract plane by vector.<br> Its operation is equals to: plane normal minus vector.
   *
   * @param vector the vector.
   * @return this plane.
   */
  public Plane subtractLocal(Vector3f vector) {
    normal.subtractLocal(vector);
    return this;
  }

  /**
   * Dot product plane with vector.
   *
   * @param point vector
   * @return dot product
   */
  public float dot(Vector3f point) {
    return normal.dot(point) - d;
  }

  /**
   * Dot product plane with plane.
   *
   * @param plane plane
   * @return dot product
   */
  public float dot(Plane plane) {
    return normal.dot(plane.normal) - d * plane.d;
  }

  /**
   * Distance between the point and the plane.
   *
   * @param point the point.
   * @param planePoint the plane point.
   * @return the distance.
   */
  public float distance(Vector3f point, Vector3f planePoint) {
    return distance(point, planePoint, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Distance between the point and the plane.
   *
   * @param point the point.
   * @param planePoint the plane point.
   * @param buffer the vector's buffer.
   * @return the distance.
   */
  public float distance(Vector3f point, Vector3f planePoint, Vector3fBuffer buffer) {
    return buffer
        .next(point)
        .subtractLocal(planePoint)
        .dot(normal);
  }

  /**
   * Distance between point and plane.
   *
   * @param point point
   * @return distance
   */
  public float distance(Vector3f point) {
    return -d + point.dot(normal);
  }

  /**
   * Angle between planes.
   *
   * @param plane plane
   * @return angle in radians
   */
  public float angle(Plane plane) {
    return ExtMath.cos(normal.dot(plane.normal) / ExtMath.sqrt(normal.sqrLength() * plane.normal.sqrLength()));
  }

  /**
   * Return true if the planes are parallel.
   *
   * @param plane the plane.
   * @param epsilon the epsilon.
   * @return true if the planes are parallel.
   */
  public boolean isParallel(Plane plane, float epsilon) {

    // check plane normals to collinearity
    var fA = plane.normal.getX() / normal.getX();
    var fB = plane.normal.getY() / normal.getY();
    var fC = plane.normal.getZ() / normal.getZ();

    return Math.abs(fA - fB) < epsilon && Math.abs(fA - fC) < epsilon;
  }

  /**
   * Return true if the planes are perpendicular.
   *
   * @param plane the plane.
   * @param epsilon the epsilon.
   * @return true if the planes are perpendicular.
   */
  public boolean isPerpendicular(Plane plane, float epsilon) {
    return Math.abs(normal.dot(plane.normal)) < epsilon;
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param startPoint the start point.
   * @param endPoint the end point.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(Vector3f startPoint, Vector3f endPoint) {
    return rayIntersection(startPoint, endPoint, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param startPoint the start point.
   * @param endPoint the end point.
   * @param buffer the vector's buffer.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(Vector3f startPoint, Vector3f endPoint, Vector3fBuffer buffer) {

    var direction = buffer.next(endPoint);
    direction.subtractLocal(startPoint);

    var denominator = direction.dot(normal);
    var distance = (d - startPoint.dot(normal)) / denominator;

    direction.multLocal(distance);

    return new Vector3f(startPoint).addLocal(direction);
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param startPoint the start point.
   * @param endPoint the end point.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(Vector3f startPoint, Vector3f endPoint, Vector3f planePoint) {
    return rayIntersection(startPoint, endPoint, planePoint, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param startPoint the start point.
   * @param endPoint the end point.
   * @param buffer the vector's buffer.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(
      Vector3f startPoint,
      Vector3f endPoint,
      Vector3f planePoint,
      Vector3fBuffer buffer) {

    var direction = buffer
        .next(endPoint)
        .subtractLocal(startPoint);

    var denominator = direction.dot(normal);
    var distance = buffer
        .next(planePoint)
        .subtractLocal(startPoint)
        .dot(normal) / denominator;

    direction.multLocal(distance);

    return new Vector3f(startPoint).addLocal(direction);
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param ray the ray.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(Ray3f ray) {
    return rayIntersection(ray, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Ray-plane intersection. Return point where ray intersect plane.<br>
   * <i>This method doesn't check plane-vector collinearity!</i>
   *
   * @param ray the ray.
   * @param buffer the vector's buffer.
   * @return the intersection point.
   */
  public Vector3f rayIntersection(Ray3f ray, Vector3fBuffer buffer) {

    var direction = ray.getDirection();
    var start = ray.getStart();

    var denominator = direction.dot(normal);
    var distance = (d - start.dot(normal)) / denominator;

    var add = buffer.next(direction);
    add.multLocal(distance);

    return new Vector3f(start).addLocal(add);
  }

  /**
   * Line-plane (segment-plane) intersection. Return point where line intersect plane.<br> If line and plane is parallel
   * or lines doesn't intersect plane return {@link Vector3f#POSITIVE_INFINITY} const.
   *
   * @param startPoint the line start point.
   * @param secondPoint the line end point.
   * @return intersection point or {@link Vector3f#POSITIVE_INFINITY}
   */
  public Vector3f lineIntersection(Vector3f startPoint, Vector3f secondPoint) {
    return lineIntersection(startPoint, secondPoint, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Line-plane (segment-plane) intersection. Return point where line intersect plane.<br> If line and plane is parallel
   * or lines doesn't intersect plane return {@link Vector3f#POSITIVE_INFINITY} const.
   *
   * @param startPoint the line start point.
   * @param secondPoint the line end point.
   * @param buffer the vector's buffer.
   * @return intersection point or {@link Vector3f#POSITIVE_INFINITY}
   */
  public Vector3f lineIntersection(
      Vector3f startPoint,
      Vector3f secondPoint,
      Vector3fBuffer buffer) {

    var ab = buffer.next(secondPoint);
    ab.subtractLocal(startPoint);

    var t = (d - normal.dot(startPoint)) / normal.dot(ab);

    if (t < 0 || t > 1.f) {
      return Vector3f.POSITIVE_INFINITY;
    }

    return new Vector3f(startPoint).addLocal(ab.multLocal(t));
  }

  /**
   * Plane-plane intersection. Return point where planes intersects.<br> If planes is parallel return
   * {@link Vector3f#POSITIVE_INFINITY} const.
   *
   * @param plane the plane.
   * @param epsilon the epsilon.
   * @return the intersection point or copy of {@link Vector3f#POSITIVE_INFINITY}.
   */
  public Vector3f planeIntersection(Plane plane, float epsilon) {
    return planeIntersection(plane, epsilon, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Plane-plane intersection. Return point where planes intersects.<br> If planes is parallel return
   * {@link Vector3f#POSITIVE_INFINITY} const.
   *
   * @param plane the plane.
   * @param epsilon the epsilon.
   * @param buffer the vector's buffer.
   * @return the intersection point or {@link Vector3f#POSITIVE_INFINITY}.
   */
  public Vector3f planeIntersection(Plane plane, float epsilon, Vector3fBuffer buffer) {

    var direction = normal.cross(plane.normal, buffer.nextVector());
    var denominator = direction.dot(direction);

    if (denominator < epsilon) {
      // these planes are parallel
      return Vector3f.POSITIVE_INFINITY;
    }

    return new Vector3f(plane.normal)
        .multLocal(d)
        .subtractLocal(buffer
            .next(normal)
            .multLocal(plane.d))
        .crossLocal(direction)
        .divideLocal(denominator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + normal.hashCode();
    result = prime * result + Float.floatToIntBits(d);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Plane{normal=" + normal + ", d=" + d + "}";
  }
}
