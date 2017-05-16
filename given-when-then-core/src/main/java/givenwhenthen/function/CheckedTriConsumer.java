package givenwhenthen.function;

public interface CheckedTriConsumer<T, U, V> {

    void accept(T t, U u, V v) throws Throwable;
}
