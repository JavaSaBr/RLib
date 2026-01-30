package javasabr.rlib.reference;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ReferencesTest {

  @Test
  void shouldReferencesWork() {

    var byteRef = ReferenceFactory.byteRef((byte) 5);
    var charRef = ReferenceFactory.charRef('T');
    var doubleRef = ReferenceFactory.doubleRef(1.5D);
    var floatRef = ReferenceFactory.floatRef(2.5F);
    var intRef = ReferenceFactory.intRef(5);
    var longRef = ReferenceFactory.longRef(7L);
    var objRef = ReferenceFactory.objRef("Val");
    var shortRef = ReferenceFactory.newShortRef((short) 7);

    assertThat(byteRef.value()).isEqualTo((byte) 5);
    assertThat(charRef.value()).isEqualTo('T');
    assertThat(doubleRef.value()).isEqualTo(1.5D);
    assertThat(floatRef.value()).isEqualTo(2.5F);
    assertThat(intRef.value()).isEqualTo(5);
    assertThat(longRef.value()).isEqualTo(7L);
    assertThat(objRef.value()).isEqualTo("Val");
    assertThat(shortRef.value()).isEqualTo((short) 7);
  }

  @Test
  void shouldTLReferencesWork() {

    var byteRef = ReferenceFactory.threadLocalByteRef((byte) 3);
    var charRef = ReferenceFactory.threadLocalCharRef('d');
    var doubleRef = ReferenceFactory.threadLocalDoubleRef(3.5D);
    var floatRef = ReferenceFactory.threadLocalFloatRef(1.5F);
    var intRef = ReferenceFactory.threadLocalIntRef(7);
    var longRef = ReferenceFactory.threadLocalLongRef(4L);
    var objRef = ReferenceFactory.threadLocalObjRef("Val3");
    var shortRef = ReferenceFactory.threadLocalShortRef((short) 2);

    assertThat(byteRef.value()).isEqualTo((byte) 3);
    assertThat(charRef.value()).isEqualTo('d');
    assertThat(doubleRef.value()).isEqualTo(3.5D);
    assertThat(floatRef.value()).isEqualTo(1.5F);
    assertThat(intRef.value()).isEqualTo(7);
    assertThat(longRef.value()).isEqualTo(4L);
    assertThat(objRef.value()).isEqualTo("Val3");
    assertThat(shortRef.value()).isEqualTo((short) 2);

    byteRef.release();
    charRef.release();
    doubleRef.release();
    floatRef.release();
    intRef.release();
    longRef.release();
    objRef.release();
    shortRef.release();

    var byteRef2 = ReferenceFactory.threadLocalByteRef((byte) 3);
    var charRef2 = ReferenceFactory.threadLocalCharRef('d');
    var doubleRef2 = ReferenceFactory.threadLocalDoubleRef(3.5D);
    var floatRef2 = ReferenceFactory.threadLocalFloatRef(1.5F);
    var intRef2 = ReferenceFactory.threadLocalIntRef(7);
    var longRef2 = ReferenceFactory.threadLocalLongRef(4L);
    var objRef2 = ReferenceFactory.threadLocalObjRef("Val3");
    var shortRef2 = ReferenceFactory.threadLocalShortRef((short) 2);

    assertThat(byteRef2).isSameAs(byteRef);
    assertThat(charRef2).isSameAs(charRef);
    assertThat(doubleRef2).isSameAs(doubleRef);
    assertThat(floatRef2).isSameAs(floatRef);
    assertThat(intRef2).isSameAs(intRef);
    assertThat(longRef2).isSameAs(longRef);
    assertThat(objRef2).isSameAs(objRef);
    assertThat(shortRef2).isSameAs(shortRef);

    var byteRef3 = ReferenceFactory.threadLocalByteRef((byte) 3);
    var charRef3 = ReferenceFactory.threadLocalCharRef('d');
    var doubleRef3 = ReferenceFactory.threadLocalDoubleRef(3.5D);
    var floatRef3 = ReferenceFactory.threadLocalFloatRef(1.5F);
    var intRef3 = ReferenceFactory.threadLocalIntRef(7);
    var longRef3 = ReferenceFactory.threadLocalLongRef(4L);
    var objRef3 = ReferenceFactory.threadLocalObjRef("Val3");
    var shortRef3 = ReferenceFactory.threadLocalShortRef((short) 2);

    assertThat(byteRef3).isNotSameAs(byteRef);
    assertThat(charRef3).isNotSameAs(charRef);
    assertThat(doubleRef3).isNotSameAs(doubleRef);
    assertThat(floatRef3).isNotSameAs(floatRef);
    assertThat(intRef3).isNotSameAs(intRef);
    assertThat(longRef3).isNotSameAs(longRef);
    assertThat(objRef3).isNotSameAs(objRef);
    assertThat(shortRef3).isNotSameAs(shortRef);
  }
}
