package givenwhenthen.function;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static givenwhenthen.function.Functions.ConsumerUnitTransformation.toBiConsumer;
import static givenwhenthen.function.Functions.ConsumerUnitTransformation.toTriConsumer;
import static givenwhenthen.function.Functions.FunctionUnitTransformation.toBiFunction;
import static givenwhenthen.function.Functions.FunctionUnitTransformation.toTriFunction;

public enum Functions {

    INSTANCE;

    public <T> Consumer<T> toConsumer(Runnable runnable) {
        return toBeConsumed -> runnable.run();
    }

    public <T, R> CheckedFunction<T, R> toFunction(CheckedConsumer<T> checkedConsumer) {
        return toBeConsumed -> {
            checkedConsumer.accept(toBeConsumed);
            return null;
        };
    }

    public <T> CheckedFunction<T, Throwable> toFunctionWithThrowableAsResult(CheckedConsumer<T> checkedConsumer) {
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

    public <$Value> Supplier<$Value> toSupplier($Value value) {
        return () -> value;
    }

    public <$Target, $Argument, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedBiFunction<$Target, $Argument, $Result> biFunction, Queue<Supplier> arguments) {
        return target -> biFunction.apply(target, ($Argument) arguments
                .remove()
                .get());
    }

    public <$Target, $Argument1, $Argument2, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedTriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
        return toFunction(toBiFunction(triFunction, arguments), arguments);
    }

    public <$Target, $Argument1, $Argument2, $Argument3, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedQuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction,
            Queue<Supplier> arguments) {
        return toFunction(toBiFunction(toTriFunction(quadriFunction, arguments), arguments), arguments);
    }

    public <$Target, $Argument> CheckedConsumer<$Target> toConsumer(CheckedBiConsumer<$Target, $Argument> biConsumer,
            Queue<Supplier> arguments) {
        return target -> biConsumer.accept(target, ($Argument) arguments
                .remove()
                .get());
    }

    public <$Target, $Argument1, $Argument2> CheckedConsumer<$Target> toConsumer(
            CheckedTriConsumer<$Target, $Argument1, $Argument2> triConsumer, Queue<Supplier> arguments) {
        return toConsumer(toBiConsumer(triConsumer, arguments), arguments);
    }

    public <$Target, $Argument1, $Argument2, $Argument3> CheckedConsumer<$Target> toConsumer(
            CheckedQuadriConsumer<$Target, $Argument1, $Argument2, $Argument3> quadriConsumer,
            Queue<Supplier> arguments) {
        return toConsumer(toBiConsumer(toTriConsumer(quadriConsumer, arguments), arguments), arguments);
    }

    static class ConsumerUnitTransformation {

        static <$Target, $Argument1, $Argument2> CheckedBiConsumer<$Target, $Argument1> toBiConsumer(
                CheckedTriConsumer<$Target, $Argument1, $Argument2> triConsumer, Queue<Supplier> arguments) {
            return (target, argument1) -> triConsumer.accept(target, argument1, ($Argument2) arguments
                    .remove()
                    .get());
        }

        static <$Target, $Argument1, $Argument2, $Argument3> CheckedTriConsumer<$Target, $Argument1, $Argument2>
        toTriConsumer(
                CheckedQuadriConsumer<$Target, $Argument1, $Argument2, $Argument3> quadriConsumer,
                Queue<Supplier> arguments) {
            return (target, argument1, argument2) -> quadriConsumer.accept(target, argument1, argument2,
                    ($Argument3) arguments
                            .remove()
                            .get());
        }
    }

    static class FunctionUnitTransformation {

        static <$Target, $Argument1, $Argument2, $Result> CheckedBiFunction<$Target, $Argument1, $Result> toBiFunction(
                CheckedTriFunction<$Target, $Argument1, $Argument2, $Result> triFunction, Queue<Supplier> arguments) {
            return (target, argument1) -> triFunction.apply(target, argument1, ($Argument2) arguments
                    .remove()
                    .get());
        }

        static <$Target, $Argument1, $Argument2, $Argument3, $Result> CheckedTriFunction<$Target, $Argument1,
                $Argument2, $Result> toTriFunction(
                CheckedQuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction,
                Queue<Supplier> arguments) {
            return (target, argument1, argument2) -> quadriFunction.apply(target, argument1, argument2,
                    ($Argument3) arguments
                            .remove()
                            .get());
        }
    }
}
