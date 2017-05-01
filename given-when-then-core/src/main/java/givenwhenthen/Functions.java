package givenwhenthen;

import java.util.function.Consumer;

class Functions {

    <T> Consumer<T> toConsumer(Runnable runnable) {
        return toBeConsumed -> runnable.run();
    }

    <T, R> CheckedFunction<T, R> toCheckedFunction(CheckedConsumer<T> checkedConsumer) {
        return toBeConsumed -> {
            checkedConsumer.accept(toBeConsumed);
            return null;
        };
    }

    <T> CheckedFunction<T, Throwable> toCheckedFunctionWithThrowableAsResult(CheckedConsumer<T> checkedConsumer) {
        return toBeConsumed -> {
            Throwable result = null;
            try {
                checkedConsumer.accept(toBeConsumed);
            } catch (Throwable throwable) {
                result = throwable;
            }
            return result;
        };
    }
}
