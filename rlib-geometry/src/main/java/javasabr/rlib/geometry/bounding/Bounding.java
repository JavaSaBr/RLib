package javasabr.rlib.geometry.bounding;

import javasabr.rlib.geometry.Quaternion4f;
import javasabr.rlib.geometry.Ray3f;
import javasabr.rlib.geometry.Vector3f;
import javasabr.rlib.geometry.Vector3fBuffer;

/**
 * Represents a bounding volume for collision detection.
 *
 * @since 10.0.0
 */
public interface Bounding {

  /**
   * Checks if this bounding volume contains the specified point.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return true if the point is contained
   * @since 10.0.0
   */
  boolean contains(float x, float y, float z);

  /**
   * Checks if this bounding volume contains the specified point.
   *
   * @param point the point to check
   * @return true if the point is contained
   * @since 10.0.0
   */
  boolean contains(Vector3f point);

  /**
   * Returns the distance from the center of this bounding to the point.
   *
   * @param point the point to measure distance to
   * @return the distance
   * @since 10.0.0
   */
  float distanceTo(Vector3f point);

  /**
   * Returns the type of this bounding volume.
   *
   * @return the bounding type
   * @since 10.0.0
   */
  BoundingType boundingType();

  /**
   * Returns the center of this bounding volume.
   *
   * @return the center vector
   * @since 10.0.0
   */
  Vector3f center();

  /**
   * Sets the center of this bounding volume.
   *
   * @param center the new center
   * @since 10.0.0
   */
  void center(Vector3f center);

  /**
   * Returns the offset of this bounding volume.
   *
   * @return the offset vector
   * @since 10.0.0
   */
  Vector3f offset();

  /**
   * Calculates and returns the result center of this bounding volume.
   *
   * @param buffer the vector buffer for temporary calculations
   * @return the result center
   * @since 10.0.0
   */
  Vector3f calculateResultCenter(Vector3fBuffer buffer);

  /**
   * Calculates the result X coordinate of the center.
   *
   * @return the result X coordinate
   * @since 10.0.0
   */
  float calculateResultCenterX();

  /**
   * Calculates the result Y coordinate of the center.
   *
   * @return the result Y coordinate
   * @since 10.0.0
   */
  float calculateResultCenterY();

  /**
   * Calculates the result Z coordinate of the center.
   *
   * @return the result Z coordinate
   * @since 10.0.0
   */
  float calculateResultCenterZ();

  /**
   * Checks if this bounding volume intersects with another bounding volume.
   *
   * @param bounding the other bounding volume
   * @param buffer the vector buffer for temporary calculations
   * @return true if the bounding volumes intersect
   * @since 10.0.0
   */
  boolean intersects(Bounding bounding, Vector3fBuffer buffer);

  /**
   * Checks if this bounding volume intersects with a ray.
   *
   * @param ray the ray to check
   * @param buffer the vector buffer for temporary calculations
   * @return true if the ray intersects this bounding volume
   * @since 10.0.0
   */
  boolean intersects(Ray3f ray, Vector3fBuffer buffer);

  /**
   * Checks if this bounding volume intersects with a ray defined by start and direction.
   *
   * @param start the ray start point
   * @param direction the ray direction
   * @param buffer the vector buffer for temporary calculations
   * @return true if the ray intersects this bounding volume
   * @since 10.0.0
   */
  boolean intersects(Vector3f start, Vector3f direction, Vector3fBuffer buffer);

  /**
   * Updates the rotation of this bounding volume.
   *
   * @param rotation the new rotation
   * @param buffer the vector buffer for temporary calculations
   * @since 10.0.0
   */
  void update(Quaternion4f rotation, Vector3fBuffer buffer);
}
