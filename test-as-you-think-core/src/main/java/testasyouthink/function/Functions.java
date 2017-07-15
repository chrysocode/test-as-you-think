/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 Xavier Pigeon and TestAsYouThink contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package testasyouthink.function;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static testasyouthink.function.Functions.ConsumerUnitTransformation.toBiConsumer;
import static testasyouthink.function.Functions.ConsumerUnitTransformation.toTriConsumer;
import static testasyouthink.function.Functions.FunctionUnitTransformation.toBiFunction;
import static testasyouthink.function.Functions.FunctionUnitTransformation.toTriFunction;

public enum Functions {

    INSTANCE;

    public <T> Consumer<T> toConsumer(Runnable runnable) {
        return toBeConsumed -> runnable.run();
    }

    public CheckedConsumer<Void> toCheckedConsumer(Runnable runnable) {
        return toBeConsumed -> runnable.run();
    }

    public <T, R> CheckedFunction<T, R> toFunction(CheckedConsumer<T> checkedConsumer) {
        return toBeConsumed -> {
            checkedConsumer.accept(toBeConsumed);
            return null;
        };
    }

    public <R> CheckedFunction<Void, R> toCheckedFunction(Supplier<R> supplier) {
        return Void -> supplier.get();
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

    public <$Value> CheckedSupplier<$Value> toCheckedSupplier($Value value) {
        return () -> value;
    }

    public <$Target, $Argument, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedBiFunction<$Target, $Argument, $Result> biFunction, Queue<CheckedSupplier> arguments) {
        return target -> biFunction.apply(target, ($Argument) arguments
                .remove()
                .get());
    }

    public <$Target, $Argument1, $Argument2, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedTriFunction<$Target, $Argument1, $Argument2, $Result> triFunction,
            Queue<CheckedSupplier> arguments) {
        return toFunction(toBiFunction(triFunction, arguments), arguments);
    }

    public <$Target, $Argument1, $Argument2, $Argument3, $Result> CheckedFunction<$Target, $Result> toFunction(
            CheckedQuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction,
            Queue<CheckedSupplier> arguments) {
        return toFunction(toBiFunction(toTriFunction(quadriFunction, arguments), arguments), arguments);
    }

    public <$Target, $Argument> CheckedConsumer<$Target> toConsumer(CheckedBiConsumer<$Target, $Argument> biConsumer,
            Queue<CheckedSupplier> arguments) {
        return target -> biConsumer.accept(target, ($Argument) arguments
                .remove()
                .get());
    }

    public <$Target, $Argument1, $Argument2> CheckedConsumer<$Target> toConsumer(
            CheckedTriConsumer<$Target, $Argument1, $Argument2> triConsumer, Queue<CheckedSupplier> arguments) {
        return toConsumer(toBiConsumer(triConsumer, arguments), arguments);
    }

    public <$Target, $Argument1, $Argument2, $Argument3> CheckedConsumer<$Target> toConsumer(
            CheckedQuadriConsumer<$Target, $Argument1, $Argument2, $Argument3> quadriConsumer,
            Queue<CheckedSupplier> arguments) {
        return toConsumer(toBiConsumer(toTriConsumer(quadriConsumer, arguments), arguments), arguments);
    }

    static class ConsumerUnitTransformation {

        static <$Target, $Argument1, $Argument2> CheckedBiConsumer<$Target, $Argument1> toBiConsumer(
                CheckedTriConsumer<$Target, $Argument1, $Argument2> triConsumer, Queue<CheckedSupplier> arguments) {
            return (target, argument1) -> triConsumer.accept(target, argument1, ($Argument2) arguments
                    .remove()
                    .get());
        }

        static <$Target, $Argument1, $Argument2, $Argument3> CheckedTriConsumer<$Target, $Argument1, $Argument2>
        toTriConsumer(
                CheckedQuadriConsumer<$Target, $Argument1, $Argument2, $Argument3> quadriConsumer,
                Queue<CheckedSupplier> arguments) {
            return (target, argument1, argument2) -> quadriConsumer.accept(target, argument1, argument2,
                    ($Argument3) arguments
                            .remove()
                            .get());
        }
    }

    static class FunctionUnitTransformation {

        static <$Target, $Argument1, $Argument2, $Result> CheckedBiFunction<$Target, $Argument1, $Result> toBiFunction(
                CheckedTriFunction<$Target, $Argument1, $Argument2, $Result> triFunction,
                Queue<CheckedSupplier> arguments) {
            return (target, argument1) -> triFunction.apply(target, argument1, ($Argument2) arguments
                    .remove()
                    .get());
        }

        static <$Target, $Argument1, $Argument2, $Argument3, $Result> CheckedTriFunction<$Target, $Argument1,
                $Argument2, $Result> toTriFunction(
                CheckedQuadriFunction<$Target, $Argument1, $Argument2, $Argument3, $Result> quadriFunction,
                Queue<CheckedSupplier> arguments) {
            return (target, argument1, argument2) -> quadriFunction.apply(target, argument1, argument2,
                    ($Argument3) arguments
                            .remove()
                            .get());
        }
    }
}
