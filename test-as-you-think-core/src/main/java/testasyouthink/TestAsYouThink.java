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

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.AbstractBigIntegerAssert;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractByteAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractCharacterAssert;
import org.assertj.core.api.AbstractClassAssert;
import org.assertj.core.api.AbstractDateAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractFileAssert;
import org.assertj.core.api.AbstractFloatAssert;
import org.assertj.core.api.AbstractFutureAssert;
import org.assertj.core.api.AbstractInputStreamAssert;
import org.assertj.core.api.AbstractInstantAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLocalDateAssert;
import org.assertj.core.api.AbstractLocalDateTimeAssert;
import org.assertj.core.api.AbstractLocalTimeAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractObjectArrayAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractPathAssert;
import org.assertj.core.api.AbstractShortAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AbstractUrlAssert;
import org.assertj.core.api.AtomicBooleanAssert;
import org.assertj.core.api.AtomicIntegerArrayAssert;
import org.assertj.core.api.AtomicIntegerAssert;
import org.assertj.core.api.AtomicLongArrayAssert;
import org.assertj.core.api.AtomicLongAssert;
import org.assertj.core.api.CompletableFutureAssert;
import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.IntPredicateAssert;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.LongPredicateAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.OptionalDoubleAssert;
import org.assertj.core.api.OptionalIntAssert;
import org.assertj.core.api.OptionalLongAssert;
import org.assertj.core.api.PredicateAssert;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGiven;
import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.execution.Execution;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedArraySupplier;
import testasyouthink.function.CheckedSuppliers.CheckedAtomicBooleanSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedAtomicIntegerArraySupplier;
import testasyouthink.function.CheckedSuppliers.CheckedAtomicIntegerSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedAtomicLongArraySupplier;
import testasyouthink.function.CheckedSuppliers.CheckedAtomicLongSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedBigDecimalSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedBigIntegerSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedBooleanSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedByteSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedCharSequenceSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedCharacterSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedClassSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedCompletableFutureSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedDateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedDoublePredicateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedDoubleSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedFileSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedFloatSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedFutureSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedInputStreamSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedInstantSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedIntPredicateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedIntegerSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedIterableSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedIteratorSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedListSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLocalDateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLocalDateTimeSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLocalTimeSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLongPredicateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedLongSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedMapSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedOptionalDoubleSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedOptionalIntSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedOptionalLongSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedOptionalSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedPathSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedPredicateSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedShortSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedStringSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedUriSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedUrlSupplier;
import testasyouthink.function.Functions;

import java.io.InputStream;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

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
        return Execution
                .of(whenStep)
                .run();
    }

    private static <$Element> $Element[] arrayAsResult(CheckedArraySupplier<$Element> whenStep) {
        return Execution
                .of(whenStep)
                .run();
    }

    public static <$ActualResult> AbstractObjectAssert<?, $ActualResult> resultOf(
            CheckedSupplier<$ActualResult> whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractCharacterAssert<?> resultOf(CheckedCharacterSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractCharSequenceAssert<?, ? extends CharSequence> resultOf(CheckedCharSequenceSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractCharSequenceAssert<?, String> resultOf(CheckedStringSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractByteAssert<?> resultOf(CheckedByteSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractShortAssert<?> resultOf(CheckedShortSupplier whenStep) {
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

    public static AbstractBigIntegerAssert<?> resultOf(CheckedBigIntegerSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractBigDecimalAssert<?> resultOf(CheckedBigDecimalSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Value> OptionalAssert<$Value> resultOf(CheckedOptionalSupplier<$Value> whenStep) {
        return assertThat(result(whenStep));
    }

    public static OptionalIntAssert resultOf(CheckedOptionalIntSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static OptionalLongAssert resultOf(CheckedOptionalLongSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static OptionalDoubleAssert resultOf(CheckedOptionalDoubleSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractBooleanAssert<?> resultOf(CheckedBooleanSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractDateAssert<?> resultOf(CheckedDateSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractLocalDateAssert<?> resultOf(CheckedLocalDateSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractLocalDateTimeAssert<?> resultOf(CheckedLocalDateTimeSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractLocalTimeAssert<?> resultOf(CheckedLocalTimeSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractInstantAssert<?> resultOf(CheckedInstantSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractFileAssert<?> resultOf(CheckedFileSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractPathAssert<?> resultOf(CheckedPathSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractUriAssert<?> resultOf(CheckedUriSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractUrlAssert<?> resultOf(CheckedUrlSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Element> IterableAssert<$Element> resultOf(CheckedIterableSupplier<$Element> whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Element> IterableAssert<$Element> resultOf(CheckedIteratorSupplier<$Element> whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Element> ListAssert<$Element> resultOf(CheckedListSupplier<$Element> whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Key, $Value> MapAssert<$Key, $Value> resultOf(CheckedMapSupplier<$Key, $Value> whenStep) {
        return assertThat(result(whenStep));
    }

    public static AtomicBooleanAssert resultOf(CheckedAtomicBooleanSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AtomicIntegerAssert resultOf(CheckedAtomicIntegerSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AtomicIntegerArrayAssert resultOf(CheckedAtomicIntegerArraySupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AtomicLongAssert resultOf(CheckedAtomicLongSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static AtomicLongArrayAssert resultOf(CheckedAtomicLongArraySupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Value> AbstractFutureAssert<?, ? extends Future<? extends $Value>, $Value> resultOf(
            CheckedFutureSupplier<$Value> whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Value> CompletableFutureAssert<$Value> resultOf(CheckedCompletableFutureSupplier<$Value> whenStep) {
        return assertThat(result(whenStep));
    }

    public static AbstractClassAssert<?> resultOf(CheckedClassSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Value> PredicateAssert<$Value> resultOf(CheckedPredicateSupplier<$Value> whenStep) {
        return assertThat(result(whenStep));
    }

    public static IntPredicateAssert resultOf(CheckedIntPredicateSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static LongPredicateAssert resultOf(CheckedLongPredicateSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static DoublePredicateAssert resultOf(CheckedDoublePredicateSupplier whenStep) {
        return assertThat(result(whenStep));
    }

    public static <$Element> AbstractObjectArrayAssert<?, $Element> resultOf(CheckedArraySupplier<$Element> whenStep) {
        return assertThat(arrayAsResult(whenStep));
    }

    public static AbstractInputStreamAssert<?, ? extends InputStream> resultOf(CheckedInputStreamSupplier whenStep) {
        return assertThat(result(whenStep));
    }
}