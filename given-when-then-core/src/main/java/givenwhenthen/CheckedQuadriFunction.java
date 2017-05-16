package givenwhenthen;

public interface CheckedQuadriFunction<T, U, V, X, R> {

    R apply(T t, U u, V v, X x) throws Throwable;
}
