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

package testasyouthink.verification;

import testasyouthink.GivenWhenContext;
import testasyouthink.execution.ExecutionError;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedPredicate;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSuppliers.CheckedBooleanSupplier;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Verification<$SystemUnderTest, $Result> {

    private static final String MISSING_EXCEPTION = "Expecting a failure, but it was missing.";
    private final GivenWhenContext<$SystemUnderTest, $Result> context;

    public Verification(GivenWhenContext<$SystemUnderTest, $Result> context) {
        this.context = context;
    }

    public void verifyResult(CheckedConsumer<$Result> expectation) {
        $Result result = context.returnResultOrVoid();
        try {
            expectation.accept(result);
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the result expectations!", throwable);
        }
    }

    public void verify(CheckedRunnable expectation) {
        context.returnResultOrVoid();
        try {
            expectation.run();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify expectations!", throwable);
        }
    }

    public void verifyResult(CheckedPredicate<$Result> expectation) {
        $Result result = context.returnResultOrVoid();
        try {
            assertThat(expectation.test(result)).isTrue();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the result expectations!", throwable);
        }
    }

    public void verifySut(CheckedPredicate<$SystemUnderTest> expectation) {
        context.returnResultOrVoid();
        try {
            assertThat(expectation.test(context.getSystemUnderTest())).isTrue();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the expectations of the system under test!", throwable);
        }
    }

    public void verifySut(CheckedConsumer<$SystemUnderTest> expectation) {
        context.returnResultOrVoid();
        try {
            expectation.accept(context.getSystemUnderTest());
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the expectations of the system under test!", throwable);
        }
    }

    public void verify(CheckedBooleanSupplier expectation) {
        context.returnResultOrVoid();
        try {
            assertThat(expectation.get()).isTrue();
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify expectations!", throwable);
        }
    }

    public void verifyResult(List<CheckedPredicate<$Result>> expectations) {
        expectations
                .stream()
                .reduce(CheckedPredicate::and)
                .ifPresent(this::verifyResult);
    }

    public void verify(Duration durationLimit) {
        context.prepareFixturesSeparately();
        Assertions
                .assertThat(context::returnResultOrVoid)
                .spendsAtMost(durationLimit);
    }

    public void verifyStdout(CheckedConsumer<File> expectations) {
        context.captureStandardStreamsSeparately();
        context.returnResultOrVoid();
        try {
            expectations.accept(context.getStdoutAsFile());
        } catch (AssertionError assertionError) {
            throw assertionError;
        } catch (Throwable throwable) {
            throw new VerificationError("Fails to verify the expectations of the stdout!", throwable);
        }
    }

    public void verifyStderr(CheckedConsumer<File> expectations) {
        context.captureStandardStreamsSeparately();
        context.returnResultOrVoid();
        try {
            expectations.accept(context.getStderrAsFile());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void verifyFailure() {
        Object result = context.returnResultOrVoid();
        if (result == null) {
            throw new AssertionError(MISSING_EXCEPTION);
        } else {
            assertThat(context.returnResultOrVoid()).isInstanceOf(Throwable.class);
        }
    }

    public void verifyFailure(Class<? extends Throwable> expectedFailureClass) {
        assertThat(context.returnResultOrVoid()).isInstanceOf(expectedFailureClass);
    }

    public void verifyFailureMessage(String expectedMessage) {
        assertThat((Throwable) context.returnResultOrVoid()).hasMessage(expectedMessage);
    }

    public void verifyFailureCause(Class<? extends Throwable> expectedCauseClass) {
        assertThat((Throwable) context.returnResultOrVoid()).hasCauseInstanceOf(expectedCauseClass);
    }

    public void verifyFailureCauseMessage(String expectedMessage) {
        assertThat(((Throwable) context.returnResultOrVoid()).getCause()).hasMessage(expectedMessage);
    }

    public void verifyNoFailure() {
        try {
            context.returnResultOrVoid();
        } catch (ExecutionError executionError) {
            assertThatThrownBy(() -> {
                throw executionError.getCause();
            }).doesNotThrowAnyException();
        }
    }
}
