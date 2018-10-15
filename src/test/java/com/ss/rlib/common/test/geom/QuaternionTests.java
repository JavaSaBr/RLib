package com.ss.rlib.common.test.geom;

import com.ss.rlib.common.geom.DirectionType;
import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Vector3f;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertTrue(originForward.equals(side, 0.001f));
        
        quaternion.getDirection(DirectionType.BEHIND, side);
        Assertions.assertTrue(originBackward.equals(side, 0.001f));
        
        quaternion.getDirection(DirectionType.RIGHT, side);
        Assertions.assertTrue(originRight.equals(side, 0.001f));
        
        quaternion.getDirection(DirectionType.LEFT, side);
        Assertions.assertTrue(originLeft.equals(side, 0.001f));
        
        quaternion.getDirection(DirectionType.UP, side);
        Assertions.assertTrue(originUp.equals(side, 0.001f));
        
        quaternion.getDirection(DirectionType.DOWN, side);
        Assertions.assertTrue(originDown.equals(side, 0.001f));
    }
}
