package givenwhenthen;

import java.util.Queue;
import java.util.function.BiConsumer;
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
        return target -> biFunction.apply(target, ($Argument) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2, $Result> BiFunction<$Target, $Argument1, $Result> toBiFunction
            (TriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
        return (target, argument1) -> triFunction.apply(target, argument1, ($Argument2) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2, $Argument3, $Result> TriFunction<$Target, $Argument1, $Argument2, $Result>
    toTriFunction(QuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction,
                  Queue<Supplier> arguments) {
        return (target, argument1, argument2) -> quadriFunction.apply(target, argument1, argument2, ($Argument3)
                arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2, $Result> CheckedFunction<$Target, $Result> toCheckedFunction
            (TriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
        return toCheckedFunction(toBiFunction(triFunction, arguments), arguments);
    }

    <$Target, $Argument1, $Argument2, $Argument3, $Result> CheckedFunction<$Target, $Result> toCheckedFunction
            (QuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction, Queue<Supplier>
                    arguments) {
        return toCheckedFunction(toBiFunction(toTriFunction(quadriFunction, arguments), arguments), arguments);
    }

    <$Target, $Argument> CheckedConsumer<$Target> toCheckedConsumer(BiConsumer<$Target, $Argument> biConsumer,
                                                                    Queue<Supplier> arguments) {
        return target -> biConsumer.accept(target, ($Argument) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2> BiConsumer<$Target, $Argument1> toBiConsumer(TriConsumer<$Target, $Argument1,
            $Argument2> triConsumer, Queue<Supplier> arguments) {
        return (target, argument1) -> triConsumer.accept(target, argument1, ($Argument2) arguments
                .remove()
                .get());
    }

    <$Target, $Argument1, $Argument2> CheckedConsumer<$Target> toCheckedConsumer(TriConsumer<$Target, $Argument1,
            $Argument2> triConsumer, Queue<Supplier> arguments) {
        return toCheckedConsumer(toBiConsumer(triConsumer, arguments), arguments);
    }
}
