package javasabr.rlib.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * QuaternionTests.java
 *
 * @author zcxv
 * @date 18.09.2018
 */
public class QuaternionTests {

  @Test
  void testDirections() {

    var originForward = new Vector3f(0.5f, 0, 1f).normalizeLocal();
    var originBackward = originForward.negate();
    var originRight = new Vector3f(1f, 0, -0.5f).normalizeLocal();
    var originLeft = originRight.negate();
    var originUp = new Vector3f(0, 1f, 0);
    var originDown = originUp.negate();

    var quaternion = new Quaternion4f();
    quaternion.lookAt(originForward, Vector3f.UNIT_Y);

    var side = quaternion.getDirection(DirectionType.FRONT);

    assertThat(originForward.equals(side, 0.001f))
        .isTrue();

    quaternion.getDirection(DirectionType.BEHIND, side);
    assertThat(originBackward.equals(side, 0.001f))
        .isTrue();

    quaternion.getDirection(DirectionType.RIGHT, side);
    assertThat(originRight.equals(side, 0.001f))
        .isTrue();

    quaternion.getDirection(DirectionType.LEFT, side);
    assertThat(originLeft.equals(side, 0.001f))
        .isTrue();

    quaternion.getDirection(DirectionType.UP, side);
    assertThat(originUp.equals(side, 0.001f))
        .isTrue();

    quaternion.getDirection(DirectionType.DOWN, side);
    assertThat(originDown.equals(side, 0.001f))
        .isTrue();
  }
}
