package givenwhenthen;

public interface CheckedQuadriConsumer<T, U, V, X> {

    void accept(T t, U u, V v, X x) throws Throwable;
}
