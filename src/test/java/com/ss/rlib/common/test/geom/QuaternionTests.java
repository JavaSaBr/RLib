package com.ss.rlib.common.test.geom;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ss.rlib.common.geom.DirectionType;
import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.Vector3fBuffer;

/**
 * QuaternionTests.java
 * 
 * @author zcxv
 * @date 18.09.2018
 * @project rlib
 */
public class QuaternionTests {

    @Test
    public void testDirections() {
        Vector3f originForward = new Vector3f(0.5f, 0, 1f).normalizeLocal();
        Vector3f originBackward = originForward.negate();
        Vector3f originRight = new Vector3f(1f, 0, -0.5f).normalizeLocal();
        Vector3f originLeft = originRight.negate();
        Vector3f originUp = new Vector3f(0, 1f, 0);
        Vector3f originDown = originUp.negate();
        
        Vector3fBuffer vb = new Vector3fBuffer() {
            @Override
            public @NotNull Vector3f nextVector() {
                return new Vector3f();
            }
        };
        
        Quaternion4f quaternion = new Quaternion4f();
        quaternion.lookAt(originForward, Vector3f.UNIT_Y, vb);
        
        Vector3f side = quaternion.getDirection(DirectionType.FRONT);
        Assertions.assertTrue(isEquals(originForward, side, 0.001f));
        
        quaternion.getDirection(DirectionType.BEHIND, side);
        Assertions.assertTrue(isEquals(originBackward, side, 0.001f));
        
        quaternion.getDirection(DirectionType.RIGHT, side);
        Assertions.assertTrue(isEquals(originRight, side, 0.001f));
        
        quaternion.getDirection(DirectionType.LEFT, side);
        Assertions.assertTrue(isEquals(originLeft, side, 0.001f));
        
        quaternion.getDirection(DirectionType.UP, side);
        Assertions.assertTrue(isEquals(originUp, side, 0.001f));
        
        quaternion.getDirection(DirectionType.DOWN, side);
        Assertions.assertTrue(isEquals(originDown, side, 0.001f));
    }
    
    private boolean isEquals(Vector3f v1, Vector3f v2, float epsilon) {
        return Math.abs(v1.getX() - v2.getX()) < epsilon &&
                Math.abs(v1.getY() - v2.getY()) < epsilon &&
                Math.abs(v1.getZ() - v2.getZ()) < epsilon;
    }
    
}
