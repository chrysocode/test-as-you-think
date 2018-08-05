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

import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThen;
import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThenStandardOutputCaptured;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedPredicate;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.verification.Verification;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class ThenStep<$SystemUnderTest, $Result> implements Then<$SystemUnderTest, $Result>,
        AndThen<$SystemUnderTest, $Result>, ThenFailure, AndThenFailure,
        AndThenStandardOutputCaptured<$SystemUnderTest, $Result> {

    private Verification<$SystemUnderTest, $Result> verification;

    ThenStep(GivenWhenContext<$SystemUnderTest, $Result> context) {
        verification = new Verification<>(context);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(CheckedConsumer<$Result> thenStep) {
        verification.verifyResult(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(CheckedRunnable thenStep) {
        verification.verify(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, CheckedConsumer<$Result> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, CheckedRunnable thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(CheckedPredicate<$Result> thenStep) {
        verification.verifyResult(thenStep);
        return this;
    }

    @Override
    public void then(List<CheckedPredicate<$Result>> thenSteps) {
        verification.verifyResult(thenSteps);
    }

    @Override
    public void then(CheckedConsumer<$Result> thenStepAboutResult,
            CheckedConsumer<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        verification.verifyResult(thenStepAboutResult);
        verification.verifySut(thenStepAboutSystemUnderTest);
    }

    @Override
    public void then(CheckedPredicate<$Result> thenStepAboutResult,
            CheckedPredicate<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        verification.verifyResult(thenStepAboutResult);
        verification.verifySut(thenStepAboutSystemUnderTest);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(CheckedConsumer<$Result> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(CheckedRunnable thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, CheckedConsumer<$Result> thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(CheckedPredicate<$Result> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, CheckedRunnable thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public AndThenFailure thenItFails() {
        verification.verifyFailure();
        return this;
    }

    @Override
    public AndThenFailure becauseOf(Class<? extends Throwable> expectedFailureClass) {
        verification.verifyFailure(expectedFailureClass);
        return this;
    }

    @Override
    public AndThenFailure withMessage(String expectedMessage) {
        verification.verifyFailureMessage(expectedMessage);
        return this;
    }

    @Override
    public AndThenFailure havingCause(Class<? extends Throwable> expectedCauseClass) {
        verification.verifyFailureCause(expectedCauseClass);
        return this;
    }

    @Override
    public AndThenFailure withCauseMessage(String expectedMessage) {
        verification.verifyFailureCauseMessage(expectedMessage);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(long timeLimit) {
        return thenSutRepliesWithin(Duration.ofMillis(timeLimit));
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(Duration durationLimit) {
        verification.verify(durationLimit);
        return this;
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> thenStandardOutput(CheckedConsumer<File> thenStep) {
        verification.verifyStdout(thenStep);
        return this;
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> thenStandardOutput(String expectationSpecification,
            CheckedConsumer<File> thenStep) {
        return thenStandardOutput(thenStep);
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> thenStandardError(CheckedConsumer<File> thenStep) {
        verification.verifyStderr(thenStep);
        return this;
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> andStandardOutput(CheckedConsumer<File> thenStep) {
        return thenStandardOutput(thenStep);
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> andStandardOutput(String expectationSpecification,
            CheckedConsumer<File> thenStep) {
        return thenStandardOutput(thenStep);
    }

    @Override
    public AndThenStandardOutputCaptured<$SystemUnderTest, $Result> andStandardError(CheckedConsumer<File> thenStep) {
        return thenStandardError(thenStep);
    }
}
