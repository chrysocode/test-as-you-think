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

import testasyouthink.GivenWhenThenDsl.Fixture.Stdin;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGiven;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGivenArgument;
import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.Functions;
import testasyouthink.preparation.Preparation;

import java.io.File;

import static java.util.Arrays.asList;

public class GivenWhenSteps<$SystemUnderTest> implements Given<$SystemUnderTest>, AndGiven<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;

    GivenWhenSteps(Class<$SystemUnderTest> sutClass) {
        preparation = new Preparation<>(sutClass);
    }

    GivenWhenSteps($SystemUnderTest systemUnderTest) {
        preparation = new Preparation<>(systemUnderTest);
    }

    GivenWhenSteps(CheckedSupplier<$SystemUnderTest> sutSupplier) {
        preparation = new Preparation<>(sutSupplier);
    }

    @Override
    public AndGiven<$SystemUnderTest> given(CheckedRunnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(CheckedConsumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, CheckedRunnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, CheckedConsumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, CheckedRunnable givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, CheckedConsumer<$SystemUnderTest> givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> givenStandardInputStream(CheckedConsumer<Stdin> givenStep) {
        preparation.recordGivenStepForStdin(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> givenStandardInputReading(final Object... inputs) {
        preparation.recordGivenStepForStdin(stdin -> stdin.expectToRead(asList(inputs)));
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> givenStandardInputReading(final File input) {
        preparation.recordGivenStepForStdin(stdin -> stdin.expectToRead(input));
        return this;
    }

    private <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> prepareArgument(
            CheckedSupplier<$Argument> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenArgumentWhenSteps<>(preparation);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
            CheckedSupplier<$Argument> givenStep) {
        return prepareArgument(givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
            Class<$Argument> mutableArgumentClass, CheckedConsumer<$Argument> givenStep) {
        preparation.recordGivenStep(mutableArgumentClass, givenStep);
        return new GivenArgumentWhenSteps<>(preparation);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            CheckedSupplier<$Argument> givenStep) {
        return givenArgument(givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            $Argument argument) {
        return prepareArgument(functions.toCheckedSupplier(argument));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
            CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return when(whenStep);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> whenSutRuns(CheckedConsumer<$SystemUnderTest> whenStep) {
        return when(whenStep);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(whenStep));
    }
}
