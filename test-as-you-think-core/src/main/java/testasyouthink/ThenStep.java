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

import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThen;
import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedPredicate;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.verification.Assertions;
import testasyouthink.verification.VerificationError;

import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenStep<$SystemUnderTest, $Result> implements Then<$SystemUnderTest, $Result>,
        AndThen<$SystemUnderTest, $Result>, ThenFailure, AndThenFailure {

    private static final String MISSING_EXCEPTION = "Expecting a failure, but it was missing.";
    private final GivenWhenContext<$SystemUnderTest, $Result> context;

    ThenStep(GivenWhenContext<$SystemUnderTest, $Result> context) {
        this.context = context;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(CheckedConsumer<$Result> thenStep) {
        try {
            thenStep.accept(context.returnResultOrVoid());
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the result expectations!", throwable);
        }
        return this;
    }

    @Override
    public void then(BiConsumer<$SystemUnderTest, $Result> thenStep) {
        thenStep.accept(context.getSystemUnderTest(), context.returnResultOrVoid());
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(CheckedRunnable thenStep) {
        context.returnResultOrVoid();
        try {
            thenStep.run();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify expectations!", throwable);
        }
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
        try {
            assertThat(thenStep.test(context.returnResultOrVoid())).isTrue();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify expectations!", throwable);
        }
        return this;
    }

    @Override
    public void then(List<Predicate<$Result>> thenSteps) {
        assertThat(thenSteps
                .stream()
                .reduce(Predicate::and)
                .get()
                .test(context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(BiPredicate<$SystemUnderTest, $Result> thenStep) {
        assertThat(thenStep.test(context.getSystemUnderTest(), context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(CheckedPredicate<$Result> thenStepAboutResult,
            CheckedPredicate<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        try {
            assertThat(thenStepAboutResult.test(context.returnResultOrVoid())).isTrue();
            assertThat(thenStepAboutSystemUnderTest.test(context.getSystemUnderTest())).isTrue();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify expectations!", throwable);
        }
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
        Object result = context.returnResultOrVoid();
        if (result == null) {
            throw new AssertionError(MISSING_EXCEPTION);
        } else {
            assertThat(context.returnResultOrVoid()).isInstanceOf(Throwable.class);
        }
        return this;
    }

    @Override
    public AndThenFailure becauseOf(Class<? extends Throwable> expectedThrowableClass) {
        assertThat(context.returnResultOrVoid()).isInstanceOf(expectedThrowableClass);
        return this;
    }

    @Override
    public AndThenFailure withMessage(String expectedMessage) {
        assertThat((Throwable) context.returnResultOrVoid()).hasMessage(expectedMessage);
        return this;
    }

    @Override
    public AndThenFailure havingCause(Class<? extends Throwable> expectedCauseClass) {
        assertThat((Throwable) context.returnResultOrVoid()).hasCauseInstanceOf(expectedCauseClass);
        return this;
    }

    @Override
    public AndThenFailure withCauseMessage(String expectedMessage) {
        assertThat(((Throwable) context.returnResultOrVoid()).getCause()).hasMessage(expectedMessage);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(long timeLimit) {
        context.prepareFixturesSeparately();
        Assertions
                .assertThat(context::returnResultOrVoid)
                .spendsAtMost(timeLimit);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> thenSutRepliesWithin(Duration durationLimit) {
        context.prepareFixturesSeparately();
        Assertions
                .assertThat(context::returnResultOrVoid)
                .spendsAtMost(durationLimit);
        return this;
    }
}
