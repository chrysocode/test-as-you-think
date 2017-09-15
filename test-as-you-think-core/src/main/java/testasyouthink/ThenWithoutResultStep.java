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

import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThenWithoutResult;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSuppliers.CheckedBooleanSupplier;
import testasyouthink.verification.Assertions;
import testasyouthink.verification.Verification;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenWithoutResultStep<$SystemUnderTest> implements ThenWithoutResult<$SystemUnderTest>,
        AndThenWithoutResult<$SystemUnderTest> {

    private final GivenWhenContext<$SystemUnderTest, Void> context;
    private final Verification<$SystemUnderTest, Void> verification;

    ThenWithoutResultStep(GivenWhenContext<$SystemUnderTest, Void> context) {
        this.context = context;
        verification = new Verification<>(context);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(CheckedRunnable thenStep) {
        verification.verify(thenStep);
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, CheckedRunnable thenStep) {
        verification.verify(thenStep);
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(CheckedConsumer<$SystemUnderTest> thenStep) {
        verification.verifySut(thenStep);
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(CheckedBooleanSupplier thenStep) {
        verification.verify(thenStep);
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(CheckedRunnable thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, CheckedRunnable thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(BooleanSupplier thenStep) {
        assertThat(thenStep.getAsBoolean()).isTrue();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification,
            CheckedConsumer<$SystemUnderTest> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification,
            Consumer<$SystemUnderTest> thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(long timeLimit) {
        context.prepareFixturesSeparately();
        Assertions
                .assertThat(context::returnResultOrVoid)
                .spendsAtMost(timeLimit);
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(Duration durationLimit) {
        context.prepareFixturesSeparately();
        Assertions
                .assertThat(context::returnResultOrVoid)
                .spendsAtMost(durationLimit);
        return this;
    }
}
