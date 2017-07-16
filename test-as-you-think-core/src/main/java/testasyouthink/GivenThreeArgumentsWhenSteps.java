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

import testasyouthink.GivenWhenThenDsl.ExecutionStage.WhenApplyingThreeArguments;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedQuadriConsumer;
import testasyouthink.function.CheckedQuadriFunction;
import testasyouthink.function.Functions;
import testasyouthink.preparation.Preparation;

public class GivenThreeArgumentsWhenSteps<$SystemUnderTest, $Argument1, $Argument2, $Argument3> implements
        WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenThreeArgumentsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(
            CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> whenSutRuns(
            CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return when(whenStep);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            CheckedQuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toFunction(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
            CheckedQuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep) {
        return when(whenStep);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(
            CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers())));
    }
}
