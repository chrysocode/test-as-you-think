package givenwhenthen;

public interface QuadriConsumer<T, U, V, X> {

    void accept(T t, U u, V v, X x);
}
