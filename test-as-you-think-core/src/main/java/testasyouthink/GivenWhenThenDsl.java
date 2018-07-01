/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 - 2018 Xavier Pigeon and TestAsYouThink contributors
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
import testasyouthink.function.CheckedPredicate;
import testasyouthink.function.CheckedQuadriConsumer;
import testasyouthink.function.CheckedQuadriFunction;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedBooleanSupplier;
import testasyouthink.function.CheckedTriConsumer;
import testasyouthink.function.CheckedTriFunction;

import java.io.File;
import java.time.Duration;
import java.util.List;

public interface GivenWhenThenDsl {

    interface PreparationStage {

        interface Given<$SystemUnderTest> extends GivenArgument<$SystemUnderTest>, When<$SystemUnderTest> {

            AndGiven<$SystemUnderTest> given(CheckedRunnable givenStep);

            AndGiven<$SystemUnderTest> given(CheckedConsumer<$SystemUnderTest> givenStep);

            AndGiven<$SystemUnderTest> given(String fixtureSpecification, CheckedRunnable givenStep);

            AndGiven<$SystemUnderTest> given(String fixtureSpecification, CheckedConsumer<$SystemUnderTest> givenStep);
        }

        interface AndGiven<$SystemUnderTest> extends GivenArgument<$SystemUnderTest>, When<$SystemUnderTest> {

            AndGiven<$SystemUnderTest> and(String fixtureSpecification, CheckedRunnable givenStep);

            AndGiven<$SystemUnderTest> and(String fixtureSpecification, CheckedConsumer<$SystemUnderTest> givenStep);
        }

        interface GivenArgument<$SystemUnderTest> {

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    CheckedSupplier<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
                    CheckedSupplier<$Argument> givenStep);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
                    $Argument argument);

            <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
                    Class<$Argument> mutableArgumentClass, CheckedConsumer<$Argument> givenStep);
        }

        interface AndGivenArgument<$SystemUnderTest, $Argument> extends WhenApplyingOneArgument<$SystemUnderTest,
                $Argument> {

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
                    CheckedSupplier<$Argument2> givenStep);

            <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
                    Class<$Argument2> mutableArgumentClass, CheckedConsumer<$Argument2> givenStep);

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
                    Class<$Argument3> mutableArgumentClass, CheckedConsumer<$Argument3> givenStep);

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

            AndThen<$SystemUnderTest, $Result> then(CheckedConsumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, CheckedConsumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> then(CheckedRunnable thenStep);

            AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, CheckedRunnable thenStep);

            AndThen<$SystemUnderTest, $Result> then(CheckedPredicate<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(long timeLimit);

            AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(Duration durationLimit);

            void then(List<CheckedPredicate<$Result>> thenSteps);

            void then(CheckedConsumer<$Result> thenStepAboutResult,
                    CheckedConsumer<$SystemUnderTest> thenStepAboutSystemUnderTest);

            void then(CheckedPredicate<$Result> thenStepAboutResult,
                    CheckedPredicate<$SystemUnderTest> thenStepAboutSystemUnderTest);

            AndThenStandardOutputCaptured<$SystemUnderTest, $Result> thenStandardOutput(CheckedConsumer<File> thenStep);
        }

        interface AndThen<$SystemUnderTest, $Result> {

            AndThen<$SystemUnderTest, $Result> and(CheckedConsumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, CheckedConsumer<$Result> thenStep);

            AndThen<$SystemUnderTest, $Result> and(CheckedRunnable thenStep);

            AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, CheckedRunnable thenStep);

            AndThen<$SystemUnderTest, $Result> and(CheckedPredicate<$Result> thenStep);
        }

        interface AndThenStandardOutputCaptured<$SystemUnderTest, $Result> extends AndThen<$SystemUnderTest, $Result> {

            AndThenStandardOutputCaptured<$SystemUnderTest, $Result> andStandardOutput(CheckedConsumer<File> thenStep);
        }

        interface ThenWithoutResult<$SystemUnderTest> {

            AndThenWithoutResult<$SystemUnderTest> then(CheckedRunnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, CheckedRunnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(CheckedConsumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification,
                    CheckedConsumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> then(CheckedBooleanSupplier thenStep);

            AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(long timeLimit);

            AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(Duration durationLimit);

            void thenItSucceeds();

            AndThenWithoutResultStandardOutputCaptured<$SystemUnderTest> thenStandardOutput(
                    CheckedConsumer<File> thenStep);
        }

        interface AndThenWithoutResult<$SystemUnderTest> {

            AndThenWithoutResult<$SystemUnderTest> and(CheckedRunnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, CheckedRunnable thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(CheckedConsumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification,
                    CheckedConsumer<$SystemUnderTest> thenStep);

            AndThenWithoutResult<$SystemUnderTest> and(CheckedBooleanSupplier thenStep);
        }

        interface AndThenWithoutResultStandardOutputCaptured<$SystemUnderTest> extends
                AndThenWithoutResult<$SystemUnderTest> {

            AndThenWithoutResultStandardOutputCaptured<$SystemUnderTest> andStandardOutput(
                    CheckedConsumer<File> thenStep);
        }

        interface ThenFailure {

            AndThenFailure thenItFails();
        }

        interface AndThenFailure {

            AndThenFailure becauseOf(Class<? extends Throwable> expectedFailureClass);

            AndThenFailure withMessage(String expectedMessage);

            AndThenFailure havingCause(Class<? extends Throwable> expectedCauseClass);

            AndThenFailure withCauseMessage(String expectedMessage);
        }
    }
}
