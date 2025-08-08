package javasabr.rlib.common.function;

@FunctionalInterface
public interface NotNullFunctionInt<T> extends FunctionInt<T> {

  int apply(T object);
}
