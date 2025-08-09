package javasabr.rlib.common.util;

import javasabr.rlib.common.util.ref.ReferenceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ReferencesTest {

  @Test
  void shouldReferencesWork() {

    var byteRef = ReferenceFactory.newByteRef((byte) 5);
    var charRef = ReferenceFactory.newCharRef('T');
    var doubleRef = ReferenceFactory.newDoubleRef(1.5D);
    var floatRef = ReferenceFactory.newFloatRef(2.5F);
    var intRef = ReferenceFactory.newIntRef(5);
    var longRef = ReferenceFactory.newLongRef(7L);
    var objRef = ReferenceFactory.newObjRef("Val");
    var shortRef = ReferenceFactory.newShortRef((short) 7);

    Assertions.assertEquals(5, byteRef.getValue());
    Assertions.assertEquals('T', charRef.getValue());
    Assertions.assertEquals(1.5D, doubleRef.getValue());
    Assertions.assertEquals(2.5F, floatRef.getValue());
    Assertions.assertEquals(5, intRef.getValue());
    Assertions.assertEquals(7L, longRef.getValue());
    Assertions.assertEquals("Val", objRef.getValue());
    Assertions.assertEquals(7, shortRef.getValue());
  }

  @Test
  void shouldReusableReferencesWork() {

    var byteRef = ReferenceFactory.newReusableByteRef((byte) 3);
    var charRef = ReferenceFactory.newReusableCharRef('d');
    var doubleRef = ReferenceFactory.newReusableDoubleRef(3.5D);
    var floatRef = ReferenceFactory.newReusableFloatRef(1.5F);
    var intRef = ReferenceFactory.newReusableIntRef(7);
    var longRef = ReferenceFactory.newReusableLongRef(4L);
    var objRef = ReferenceFactory.newReusableObjRef("Val3");
    var shortRef = ReferenceFactory.newReusableShortRef((short) 2);

    Assertions.assertEquals(3, byteRef.getValue());
    Assertions.assertEquals('d', charRef.getValue());
    Assertions.assertEquals(3.5D, doubleRef.getValue());
    Assertions.assertEquals(1.5F, floatRef.getValue());
    Assertions.assertEquals(7, intRef.getValue());
    Assertions.assertEquals(4L, longRef.getValue());
    Assertions.assertEquals("Val3", objRef.getValue());
    Assertions.assertEquals(2, shortRef.getValue());

    byteRef.release();
    charRef.release();
    doubleRef.release();
    floatRef.release();
    intRef.release();
    longRef.release();
    objRef.release();
    shortRef.release();

    Assertions.assertEquals(0, byteRef.getValue());
    Assertions.assertEquals(0, charRef.getValue());
    Assertions.assertEquals(0, doubleRef.getValue());
    Assertions.assertEquals(0, floatRef.getValue());
    Assertions.assertEquals(0, intRef.getValue());
    Assertions.assertEquals(0, longRef.getValue());
    Assertions.assertNull(objRef.getValue());
    Assertions.assertEquals(0, shortRef.getValue());
    Assertions.assertEquals(0, byteRef.getValue());

    var byteRef2 = ReferenceFactory.newReusableByteRef((byte) 3);
    var charRef2 = ReferenceFactory.newReusableCharRef('d');
    var doubleRef2 = ReferenceFactory.newReusableDoubleRef(3.5D);
    var floatRef2 = ReferenceFactory.newReusableFloatRef(1.5F);
    var intRef2 = ReferenceFactory.newReusableIntRef(7);
    var longRef2 = ReferenceFactory.newReusableLongRef(4L);
    var objRef2 = ReferenceFactory.newReusableObjRef("Val3");
    var shortRef2 = ReferenceFactory.newReusableShortRef((short) 2);

    Assertions.assertSame(byteRef, byteRef2);
    Assertions.assertSame(charRef, charRef2);
    Assertions.assertSame(doubleRef, doubleRef2);
    Assertions.assertSame(floatRef, floatRef2);
    Assertions.assertSame(intRef, intRef2);
    Assertions.assertSame(longRef, longRef2);
    Assertions.assertSame(objRef, objRef2);
    Assertions.assertSame(shortRef, shortRef2);
  }

  @Test
  void shouldTLReferencesWork() {

    var byteRef = ReferenceFactory.newThreadLocalByteRef((byte) 3);
    var charRef = ReferenceFactory.newThreadLocalCharRef('d');
    var doubleRef = ReferenceFactory.newThreadLocalDoubleRef(3.5D);
    var floatRef = ReferenceFactory.newThreadLocalFloatRef(1.5F);
    var intRef = ReferenceFactory.newThreadLocalIntRef(7);
    var longRef = ReferenceFactory.newThreadLocalLongRef(4L);
    var objRef = ReferenceFactory.newThreadLocalObjRef("Val3");
    var shortRef = ReferenceFactory.newThreadLocalShortRef((short) 2);

    Assertions.assertEquals(3, byteRef.getValue());
    Assertions.assertEquals('d', charRef.getValue());
    Assertions.assertEquals(3.5D, doubleRef.getValue());
    Assertions.assertEquals(1.5F, floatRef.getValue());
    Assertions.assertEquals(7, intRef.getValue());
    Assertions.assertEquals(4L, longRef.getValue());
    Assertions.assertEquals("Val3", objRef.getValue());
    Assertions.assertEquals(2, shortRef.getValue());

    byteRef.release();
    charRef.release();
    doubleRef.release();
    floatRef.release();
    intRef.release();
    longRef.release();
    objRef.release();
    shortRef.release();

    Assertions.assertEquals(0, byteRef.getValue());
    Assertions.assertEquals(0, charRef.getValue());
    Assertions.assertEquals(0, doubleRef.getValue());
    Assertions.assertEquals(0, floatRef.getValue());
    Assertions.assertEquals(0, intRef.getValue());
    Assertions.assertEquals(0, longRef.getValue());
    Assertions.assertNull(objRef.getValue());
    Assertions.assertEquals(0, shortRef.getValue());
    Assertions.assertEquals(0, byteRef.getValue());

    var byteRef2 = ReferenceFactory.newThreadLocalByteRef((byte) 3);
    var charRef2 = ReferenceFactory.newThreadLocalCharRef('d');
    var doubleRef2 = ReferenceFactory.newThreadLocalDoubleRef(3.5D);
    var floatRef2 = ReferenceFactory.newThreadLocalFloatRef(1.5F);
    var intRef2 = ReferenceFactory.newThreadLocalIntRef(7);
    var longRef2 = ReferenceFactory.newThreadLocalLongRef(4L);
    var objRef2 = ReferenceFactory.newThreadLocalObjRef("Val3");
    var shortRef2 = ReferenceFactory.newThreadLocalShortRef((short) 2);

    Assertions.assertSame(byteRef, byteRef2);
    Assertions.assertSame(charRef, charRef2);
    Assertions.assertSame(doubleRef, doubleRef2);
    Assertions.assertSame(floatRef, floatRef2);
    Assertions.assertSame(intRef, intRef2);
    Assertions.assertSame(longRef, longRef2);
    Assertions.assertSame(objRef, objRef2);
    Assertions.assertSame(shortRef, shortRef2);
  }
}
