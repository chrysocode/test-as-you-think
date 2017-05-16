package givenwhenthen;

public interface CheckedTriFunction<T, U, V, R> {

    R apply(T t, U u, V v) throws Throwable;
}
