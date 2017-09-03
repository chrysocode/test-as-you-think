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

import testasyouthink.execution.Execution;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.preparation.Preparation;

enum ThenStepFactory {

    INSTANCE;

    <$SystemUnderTest, $Result> ThenStep<$SystemUnderTest, $Result> createThenStep(
            Preparation<$SystemUnderTest> preparation, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        Execution<$SystemUnderTest, $Result> execution = new Execution<>(preparation.supplySut(), whenStep);
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, execution);
        return new ThenStep<>(context);
    }

    <$SystemUnderTest> ThenWithoutResultStep<$SystemUnderTest> createThenStep(Preparation<$SystemUnderTest> preparation,
            CheckedConsumer<$SystemUnderTest> whenStep) {
        Execution<$SystemUnderTest, Void> execution = new Execution<>(preparation.supplySut(), whenStep);
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, execution);
        return new ThenWithoutResultStep<>(context);
    }

    private Preparation<Void> nothingToPrepare() {
        return new Preparation<>();
    }

    <$Result> ThenStep<Void, $Result> createThenStep(CheckedFunction<Void, $Result> whenStep) {
        return createThenStep(nothingToPrepare(), whenStep);
    }

    ThenWithoutResultStep<Void> createThenStep(CheckedConsumer<Void> whenStep) {
        return createThenStep(nothingToPrepare(), whenStep);
    }
}
