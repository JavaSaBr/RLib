package javasabr.rlib.geometry;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author zcxv
 */
@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Polygon {

  private final static float EPSILON_ON_PLANE = 0.1f;
  private final static float EPSILON_CWW = 0.01f;

  final Vector3f[] vertices;
  final Plane plane;

  int flags;

  /**
   * Construct polygon from the vertices.
   *
   * @param vertices the vertices.
   * @throws IllegalArgumentException throw if vertices is less than 3
   * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f)
   */
  public Polygon(Vector3f[] vertices) throws IllegalArgumentException {
    this(vertices, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Construct polygon from the vertices.
   *
   * @param vertices the vertices.
   * @param buffer the vector's buffer.
   * @throws IllegalArgumentException throw if vertices is less than 3
   * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f, Vector3fBuffer)
   */
  public Polygon(Vector3f[] vertices, Vector3fBuffer buffer) throws IllegalArgumentException {

    if (vertices.length < 3) {
      throw new IllegalArgumentException("polygon cannot have less than 3 vertices.");
    }

    this.vertices = vertices;

    var normal = buffer.next();

    for (int i = 2; i < vertices.length; i++) {
      var ab = buffer
          .next(vertices[i - 1])
          .subtractLocal(vertices[0]);
      var ac = buffer
          .next(vertices[i])
          .subtractLocal(vertices[0]);
      normal.addLocal(ab.crossLocal(ac));
    }

    this.plane = new Plane(planePoint(), normal.normalizeLocal());
  }

  /**
   * Construct polygon from the 3 vertices.
   *
   * @param first the first vertex.
   * @param second the second vertex.
   * @param third the third vertex.
   * @param buffer the vector's buffer.
   * @see Polygon#Polygon(Vector3f[], Vector3fBuffer)
   */
  public Polygon(
      Vector3f first,
      Vector3f second,
      Vector3f third,
      Vector3fBuffer buffer) {
    this(
        new Vector3f[]{
            first,
            second,
            third
        }, buffer);
  }

  public Polygon(Vector3f first, Vector3f second, Vector3f third) {
    this(
        new Vector3f[]{
            first,
            second,
            third
        }, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Return plane point.<br> It's a first polygon vertex.
   *
   * @return the plane point.
   */
  public Vector3f planePoint() {
    return vertices[0];
  }

  public void flags(int flags) {
    this.flags = flags;
  }

  public boolean toggleFlag(int flag) {
    flags ^= flag;
    return isFlagSet(flag);
  }

  public boolean isFlagSet(int flag) {
    return (flags & flag) != 0;
  }

  public void setFlag(int flag) {
    flags |= flag;
  }

  public void unsetFlag(int flag) {
    flags &= ~flag;
  }

  public Vector3f calculateMidPoint() {
    return calculateMidPoint(Vector3fBuffer.NO_REUSE);
  }

  public Vector3f calculateMidPoint(Vector3fBuffer buffer) {

    var point = buffer.next();

    for (var vertice : vertices) {
      point.addLocal(vertice);
    }

    return point.divideLocal(vertices.length);
  }

  public boolean calculateIsCoplanar() {
    return calculateIsCoplanar(Vector3fBuffer.NO_REUSE);
  }

  public boolean calculateIsCoplanar(Vector3fBuffer buffer) {

    for (var vertice : vertices) {
      if (!isOnPlane(vertice)) {
        return false;
      }
    }

    return true;
  }

  public boolean isOnPlane(Vector3f point) {
    var distance = plane.distance(point);
    return distance > -EPSILON_ON_PLANE && distance < EPSILON_ON_PLANE;
  }

  /**
   * Determines if the line segment intersects this polygon.
   *
   * @param startLine the start point of the line
   * @param endLine the end point of the line
   * @param point output parameter for intersection coordinates, or null
   * @return true if the line intersects the polygon
   * @since 10.0.0
   */
  public boolean intersect(Vector3f startLine, Vector3f endLine, Vector3f point) {
    return intersect(startLine, endLine, point, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Determines if the line segment intersects this polygon.
   *
   * @param startLine the start point of the line
   * @param endLine the end point of the line
   * @param point output parameter for intersection coordinates, or null
   * @param vectorBuffer the vector buffer for temporary calculations
   * @return true if the line intersects the polygon
   * @since 10.0.0
   */
  public boolean intersect(
      Vector3f startLine,
      Vector3f endLine,
      @Nullable Vector3f point,
      Vector3fBuffer vectorBuffer) {

    var aDistance = plane.distance(startLine, vertices[0], vectorBuffer);
    var bDistance = plane.distance(endLine, vertices[0], vectorBuffer);

    if ((aDistance < 0 && bDistance < 0) || (aDistance > 0 && bDistance > 0)) {
      return false;
    }

    var intersection = plane.rayIntersection(startLine, endLine, vertices[0], vectorBuffer);

    if (intersection.equals(startLine, 0.1f) || intersection.equals(endLine, 0.1f)) {
      return false;
    }

    if (point != null) {
      point.set(intersection);
    }

    return contains(intersection, vectorBuffer);
  }

  /**
   * Determines if point inside of polygon.
   *
   * @param point point
   * @return true if point inside
   */
  public boolean contains(Vector3f point) {
    return contains(point, Vector3fBuffer.NO_REUSE);
  }

  /**
   * Determines if point inside of polygon.
   *
   * @param point point
   * @param buffer vector's buffer
   * @return true if point inside
   */
  public boolean contains(Vector3f point, Vector3fBuffer buffer) {

    int low = 0;
    int high = vertices.length;

    do {

      int mid = (low + high) / 2;

      if (isTriangleCCW(vertices[0], vertices[mid], point, buffer)) {
        low = mid;
      } else {
        high = mid;
      }

    } while (low + 1 < high);

    if (low == 0 || high == vertices.length) {
      return false;
    }

    return isTriangleCCW(vertices[low], vertices[high], point, buffer);
  }

  /**
   * Determines if triangle specified by 3 points is defined counterclockwise.
   *
   * @param first first vertex.
   * @param second second vertex.
   * @param third third vertex.
   * @param buffer vector's buffer.
   * @return true if defined.
   */
  private boolean isTriangleCCW(
      Vector3f first,
      Vector3f second,
      Vector3f third,
      Vector3fBuffer buffer) {

    var ab = buffer
        .next(first)
        .subtractLocal(second);

    var ac = buffer
        .next(first)
        .subtractLocal(third);

    ab.crossLocal(ac);

    return plane
        .normal()
        .dot(ab) > EPSILON_CWW;
  }

  @Override
  public String toString() {
    return "Polygon{vertices=" + Arrays.toString(vertices) + ", plane=" + plane + ", flags=" + flags + "}";
  }
}
