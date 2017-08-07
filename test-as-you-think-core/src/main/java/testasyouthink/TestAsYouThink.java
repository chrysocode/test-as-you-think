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

package testasyouthink;

import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractByteAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractCharacterAssert;
import org.assertj.core.api.AbstractDateAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractFloatAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLongAssert;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGiven;
import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.execution.ExecutionError;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedBooleanSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedByteSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedCharacterSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedDateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedDoubleSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedFloatSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedIntegerSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLongSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedStringSupplier;
import testasyouthink.function.Functions;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static testasyouthink.execution.Event.EXECUTION_FAILURE_MESSAGE;

public class TestAsYouThink {

    private static Functions functions = Functions.INSTANCE;
    private static ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;

    private TestAsYouThink() {}

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new GivenWhenSteps<>(systemUnderTest);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut(Supplier<$SystemUnderTest> givenSutStep) {
        return new GivenWhenSteps<>(givenSutStep);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass) {
        return new GivenWhenSteps<>(sutClass);
    }

    public static <$SystemUnderTest> AndGiven<$SystemUnderTest> givenSut(Class<$SystemUnderTest> sutClass,
            Consumer<$SystemUnderTest> givenStep) {
        return givenSutClass(sutClass).given(givenStep);
    }

    public static ThenWithoutResult<Void> when(Runnable whenStep) {
        return thenStepFactory.createThenStep(functions.toCheckedConsumer(whenStep));
    }

    public static <$Result> Then<Void, $Result> when(Supplier<$Result> whenStep) {
        return thenStepFactory.createThenStep(functions.toCheckedFunction(whenStep));
    }

    public static <$SystemUnderTest, $Result> CheckedFunction<$SystemUnderTest, $Result> withReturn(
            CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return whenStep;
    }

    public static ThenFailure whenOutsideOperatingConditions(CheckedRunnable whenStep) {
        return thenStepFactory.createThenStep(functions.toFunctionWithThrowableAsResult(whenStep));
    }

    private static <$Result> $Result result(CheckedSupplier<$Result> whenStep) {
        $Result result;
        try {
            result = whenStep.get();
        } catch (Throwable throwable) {
            throw new ExecutionError(EXECUTION_FAILURE_MESSAGE, throwable);
        }
        return result;
    }

    public static AbstractCharacterAssert<?> resultOf(CheckedCharacterSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractCharSequenceAssert<?, String> resultOf(CheckedStringSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractByteAssert<?> resultOf(CheckedByteSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractIntegerAssert<?> resultOf(CheckedIntegerSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractLongAssert<?> resultOf(CheckedLongSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractFloatAssert<?> resultOf(CheckedFloatSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractDoubleAssert<?> resultOf(CheckedDoubleSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractBooleanAssert<?> resultOf(CheckedBooleanSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractDateAssert<?> resultOf(CheckedDateSupplier whenStep) {
        return assertThat(result(whenStep));
    }
}
