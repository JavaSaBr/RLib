package javasabr.rlib.common;

public class BaseTest {

  protected static class Type1 {
    public static final Type1 EXAMPLE = new Type1();
  }

  protected static class Type2 {
    public static final Type2 EXAMPLE = new Type2();
  }

  public <T> void assertType(T object, Class<T> type) {
    if (!type.isInstance(object)) {
      throw new ClassCastException();
    }
  }

  public void assertIntType(Integer val) {
  }

  public void assertLongType(Long val) {
  }

  public void assertFloatType(Float val) {
  }
}
