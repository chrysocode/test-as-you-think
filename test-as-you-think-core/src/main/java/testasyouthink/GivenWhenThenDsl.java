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

import testasyouthink.GivenWhenThenDsl.ExecutionStage.When;
import testasyouthink.GivenWhenThenDsl.ExecutionStage.WhenApplyingOneArgument;
import testasyouthink.GivenWhenThenDsl.ExecutionStage.WhenApplyingThreeArguments;
import testasyouthink.GivenWhenThenDsl.ExecutionStage.WhenApplyingTwoArguments;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedBiConsumer;
import testasyouthink.function.CheckedBiFunction;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.CheckedQuadriConsumer;
import testasyouthink.function.CheckedQuadriFunction;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.CheckedTriConsumer;
import testasyouthink.function.CheckedTriFunction;

import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GivenWhenThenDsl {

    interface PreparationStage {

        interface Given<$SystemUnderTest> extends When<$SystemUnderTest> {

            AndGiven<$SystemUnderTest> given(Runnable givenStep);

            AndGiven<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep);

            AndGiven<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep);

            AndGiven<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    CheckedSupplier<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    Class<$Argument> immutableArgumentClass, CheckedFunction<$Argument, $Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    Class<$Argument> mutableArgumentClass, Consumer<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
                    CheckedSupplier<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
                    $Argument argument);
        }

        interface AndGiven<$SystemUnderTest> extends When<$SystemUnderTest> {

            AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep);

            AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    CheckedSupplier<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
                    $Argument argument);
        }

        interface AndGivenArgument<$SystemUnderTest, $Argument> extends WhenApplyingOneArgument<$SystemUnderTest,
                $Argument> {

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
                    CheckedSupplier<$Argument2> givenStep);

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
                    Class<$Argument2> mutableArgumentClass, Consumer<$Argument2> givenStep);

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
                    Class<$Argument2> immutableArgumentClass, CheckedFunction<$Argument2, $Argument2> givenStep);

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(String description,
                    CheckedSupplier<$Argument2> givenStep);

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(String description,
                    $Argument2 argument);
        }

        interface AndGivenTwoArguments<$SystemUnderTest, $Argument1, $Argument2> extends
                WhenApplyingTwoArguments<$SystemUnderTest, $Argument1, $Argument2> {

            <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
                    CheckedSupplier<$Argument3> givenStep);

            <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
                    Class<$Argument3> immutableArgumentClass, CheckedFunction<$Argument3, $Argument3> givenStep);

            <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
                    String description, CheckedSupplier<$Argument3> givenStep);

            <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
                    String description, $Argument3 argument);
        }
    }

    interface ExecutionStage {

        interface When<$SystemUnderTest> {

            <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
                    CheckedFunction<$SystemUnderTest, $Result> whenStep);

            ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep);

            ThenWithoutResult<$SystemUnderTest> whenSutRuns(CheckedConsumer<$SystemUnderTest> whenStep);

            ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep);
        }

        interface WhenApplyingOneArgument<$SystemUnderTest, $Argument> {

            ThenWithoutResult<$SystemUnderTest> when(CheckedBiConsumer<$SystemUnderTest, $Argument> whenStep);

            ThenWithoutResult<$SystemUnderTest> whenSutRuns(CheckedBiConsumer<$SystemUnderTest, $Argument> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> when(
                    CheckedBiFunction<$SystemUnderTest, $Argument, $Result> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
                    CheckedBiFunction<$SystemUnderTest, $Argument, $Result> whenStep);

            ThenFailure whenSutRunsOutsideOperatingConditions(CheckedBiConsumer<$SystemUnderTest, $Argument> whenStep);
        }

        interface WhenApplyingTwoArguments<$SystemUnderTest, $Argument1, $Argument2> {

            ThenWithoutResult<$SystemUnderTest> when(
                    CheckedTriConsumer<$SystemUnderTest, $Argument1, $Argument2> whenStep);

            ThenWithoutResult<$SystemUnderTest> whenSutRuns(
                    CheckedTriConsumer<$SystemUnderTest, $Argument1, $Argument2> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> when(
                    CheckedTriFunction<$SystemUnderTest, $Argument1, $Argument2, $Result> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
                    CheckedTriFunction<$SystemUnderTest, $Argument1, $Argument2, $Result> whenStep);

            ThenFailure whenSutRunsOutsideOperatingConditions(
                    CheckedTriConsumer<$SystemUnderTest, $Argument1, $Argument2> whenStep);
        }

        interface WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> {

            ThenWithoutResult<$SystemUnderTest> when(
                    CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep);

            ThenWithoutResult<$SystemUnderTest> whenSutRuns(
                    CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> when(
                    CheckedQuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep);

            <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
                    CheckedQuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep);

            ThenFailure whenSutRunsOutsideOperatingConditions(
                    CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep);
        }
    }

    interface VerificationStage {

        interface Then<$SystemUnderTest, $Result> {

            AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> then(Runnable thenStep);

            AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Runnable thenStep);

            AndThen<$SystemUnderTest, $Result> then(Predicate<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(long timeLimit);

            AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(Duration duration);

            void then(List<Predicate<$Result>> thenSteps);

            void then(BiConsumer<$SystemUnderTest, $Result> thenStep);

            void then(BiPredicate<$SystemUnderTest, $Result> thenStep);

            void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest);
        }

        interface AndThen<$SystemUnderTest, $Result> {

            AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> and(Runnable thenStep);

            AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Runnable thenStep);

            AndThen<$SystemUnderTest, $Result> and(Predicate<$Result> thenStep);
        }

        interface ThenWithoutResult<$SystemUnderTest> {

            AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Runnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(Consumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification,
                    Consumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(BooleanSupplier thenStep);

            AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(long timeLimit);

            AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(Duration duration);
        }

        interface AndThenWithoutResult<$SystemUnderTest> {

            AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Runnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification,
                    Consumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(BooleanSupplier thenStep);
        }

        interface ThenFailure {

            ThenFailureWithExpectedException thenItFails();
        }

        interface ThenFailureWithExpectedException {

            ThenFailureWithExpectedMessage becauseOf(Class<? extends Throwable> expectedThrowableClass);
        }

        interface ThenFailureWithExpectedMessage {

            void withMessage(String expectedMessage);
        }
    }
}
