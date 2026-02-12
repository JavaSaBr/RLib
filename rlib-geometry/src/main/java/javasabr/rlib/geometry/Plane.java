package javasabr.rlib.geometry;

import static javasabr.rlib.geometry.Vector3f.substract;

import javasabr.rlib.common.util.ExtMath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Represents a 3D plane defined by the formula: Ax + By + Cz + D = 0.
 *
 * @since 10.0.0
 */
@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Plane {

  final Vector3f normal;

  float dot;

  public Plane(Vector3f first, Vector3f second, Vector3f third) {

    var ba = substract(second, first);
    var ca = substract(third, first);

    normal = ba
        .cross(ca)
        .normalizeLocal();
    dot = first.dot(normal);
  }

  public Plane(Vector3f planePoint, Vector3f normal) {
    this.normal = normal.clone();
    this.dot = planePoint.dot(normal);
  }

  public Plane multLocal(float scalar) {
    normal.multLocal(scalar);
    dot *= scalar;
    return this;
  }

  public Plane divideLocal(float scalar) {
    normal.divideLocal(scalar);
    dot /= scalar;
    return this;
  }

  public Plane addLocal(float x, float y, float z, float dot) {
    normal.addLocal(x, y, z);
    this.dot += dot;
    return this;
  }

  public Plane subtractLocal(float x, float y, float z, float dot) {
    normal.subtractLocal(x, y, z);
    this.dot -= dot;
    return this;
  }

  public Plane multLocal(Plane plane) {
    normal.multLocal(plane.normal);
    dot *= plane.dot;
    return this;
  }

  public Plane divideLocal(Plane plane) {
    normal.divideLocal(plane.normal);
    dot /= plane.dot;
    return this;
  }

  public Plane addLocal(Plane plane) {
    normal.addLocal(plane.normal);
    dot += plane.dot;
    return this;
  }

  public Plane subtractLocal(Plane plane) {
    normal.subtractLocal(plane.normal);
    dot -= plane.dot;
    return this;
  }

  public Plane multLocal(Vector3f vector) {
    normal.multLocal(vector);
    return this;
  }

  public Plane divideLocal(Vector3f vector) {
    normal.divideLocal(vector);
    return this;
  }

  public Plane addLocal(Vector3f vector) {
    normal.addLocal(vector);
    return this;
  }

  public Plane subtractLocal(Vector3f vector) {
    normal.subtractLocal(vector);
    return this;
  }

  public float dot(Vector3f point) {
    return normal.dot(point) - dot;
  }

  public float dot(Plane plane) {
    return normal.dot(plane.normal) - dot * plane.dot;
  }

  /**
   * Distance between the point and the plane.
   */
  public float distance(Vector3f point, Vector3f planePoint) {
    return distance(point, planePoint, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Distance between the point and the plane.
   */
  public float distance(Vector3f point, Vector3f planePoint, Vector3fBuffer buffer) {
    return buffer
        .next(point)
        .subtractLocal(planePoint)
        .dot(normal);
  }

  /**
   * Distance between point and plane.
   */
  public float distance(Vector3f point) {
    return -dot + point.dot(normal);
  }

  /**
   * Angle between planes.
   */
  public float angle(Plane plane) {
    return ExtMath.cos(normal.dot(plane.normal) /
        ExtMath.sqrt(normal.sqrLength() * plane.normal.sqrLength()));
  }

  /**
   * Return true if the planes are parallel.
   */
  public boolean isParallel(Plane plane, float epsilon) {

    // check plane normals to collinearity
    var fA = plane.normal.x() / normal.x();
    var fB = plane.normal.y() / normal.y();
    var fC = plane.normal.z() / normal.z();

    return Math.abs(fA - fB) < epsilon && Math.abs(fA - fC) < epsilon;
  }

  /**
   * Return true if the planes are perpendicular.
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
    var distance = (dot - startPoint.dot(normal)) / denominator;

    direction.multLocal(distance);

    return buffer
        .next(startPoint)
        .addLocal(direction);
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

    return buffer
        .next(startPoint)
        .addLocal(direction);
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

    var direction = ray.direction();
    var start = ray.start();

    var denominator = direction.dot(normal);
    var distance = (dot - start.dot(normal)) / denominator;

    var add = buffer.next(direction);
    add.multLocal(distance);

    return buffer
        .next(start)
        .addLocal(add);
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

    var t = (dot - normal.dot(startPoint)) / normal.dot(ab);

    if (t < 0 || t > 1.f) {
      return Vector3f.POSITIVE_INFINITY;
    }

    return buffer
        .next(startPoint)
        .addLocal(ab.multLocal(t));
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

    var direction = normal.cross(plane.normal, buffer.next());
    var denominator = direction.dot(direction);

    if (denominator < epsilon) {
      // these planes are parallel
      return Vector3f.POSITIVE_INFINITY;
    }

    return buffer
        .next(plane.normal)
        .multLocal(dot)
        .subtractLocal(buffer
            .next(normal)
            .multLocal(plane.dot))
        .crossLocal(direction)
        .divideLocal(denominator);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + normal.hashCode();
    result = prime * result + Float.floatToIntBits(dot);
    return result;
  }

  @Override
  public String toString() {
    return "Plane{normal=" + normal + ", d=" + dot + "}";
  }
}
