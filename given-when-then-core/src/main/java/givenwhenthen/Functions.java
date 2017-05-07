package givenwhenthen;

import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

enum Functions {

    INSTANCE;

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

    <$Target, $Argument, $Result> CheckedFunction<$Target, $Result> toCheckedFunction(BiFunction<$Target, $Argument,
            $Result> biFunction, Queue<Supplier> arguments) {
        return sut -> biFunction.apply(sut, ($Argument) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2, $Result> BiFunction<$Target, $Argument1, $Result> toBiFunction
            (TriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
        return (sut, argument1) -> triFunction.apply(sut, argument1, ($Argument2) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2, $Result> CheckedFunction<$Target, $Result> toCheckedFunction
            (TriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
        return toCheckedFunction(toBiFunction(triFunction, arguments), arguments);
    }
}
