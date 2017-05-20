package givenwhenthen.function;

@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Throwable;
}
