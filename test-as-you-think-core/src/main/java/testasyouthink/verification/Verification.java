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
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Verification<$SystemUnderTest, $Result> {

    private final GivenWhenContext<$SystemUnderTest, $Result> context;

    public Verification(GivenWhenContext<$SystemUnderTest, $Result> context) {
        this.context = context;
    }

    public void verifyResult(final CheckedConsumer<$Result> expectation) {
        defaultResult()
                .expectToSatisfy(expectation)
                .orFail("Fails to verify the result expectations!");
    }

    public void verify(final CheckedRunnable expectation) {
        defaultResult()
                .expectToSatisfy(result -> expectation.run())
                .orFail("Fails to verify expectations!");
    }

    public void verifyResult(final CheckedPredicate<$Result> expectation) {
        defaultResult()
                .expectToSatisfy(result -> assertThat(expectation.test(result)).isTrue())
                .orFail("Fails to verify the result expectations!");
    }

    public void verifySut(final CheckedPredicate<$SystemUnderTest> expectation) {
        actualResult(context::getSystemUnderTest)
                .expectToSatisfy(result -> assertThat(expectation.test(context.getSystemUnderTest())).isTrue())
                .orFail("Fails to verify the expectations of the system under test!");
    }

    public void verifySut(final CheckedConsumer<$SystemUnderTest> expectation) {
        actualResult(context::getSystemUnderTest)
                .expectToSatisfy(expectation)
                .orFail("Fails to verify the expectations of the system under test!");
    }

    public void verify(final CheckedBooleanSupplier expectation) {
        defaultResult()
                .expectToSatisfy(result -> assertThat(expectation.get()).isTrue())
                .orFail("Fails to verify expectations!");
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

    public void verifyStdout(final CheckedConsumer<File> expectation) {
        actualResult(context::getStdoutAsFile)
                .toDoBefore(context::captureStandardStreamsSeparately)
                .expectToSatisfy(expectation)
                .orFail("Fails to verify the expectations of the stdout!");
    }

    public void verifyStderr(final CheckedConsumer<File> expectation) {
        actualResult(context::getStderrAsFile)
                .toDoBefore(context::captureStandardStreamsSeparately)
                .expectToSatisfy(expectation)
                .orFail("Fails to verify the expectations of the stderr!");
    }

    public void verifyStandardStreams(final CheckedConsumer<File> expectation) {
        actualResult(context::getStdStreamsAsFile)
                .toDoBefore(context::captureStandardStreamsTogether)
                .expectToSatisfy(expectation)
                .orFail("Fails to verify the expectations of the standard streams!");
    }

    public void verifyFailure() {
        if (actualFailure() == null) {
            throw new AssertionError("Expecting a failure, but it was missing.");
        } else {
            assertThat(actualFailure()).isInstanceOf(Throwable.class);
        }
    }

    public void verifyFailure(Class<? extends Throwable> expectedFailureClass) {
        assertThat(actualFailure()).isInstanceOf(expectedFailureClass);
    }

    public void verifyFailureMessage(String expectedMessage) {
        assertThat(actualFailure()).hasMessage(expectedMessage);
    }

    public void verifyFailureCause(Class<? extends Throwable> expectedCauseClass) {
        assertThat(actualFailure()).hasCauseInstanceOf(expectedCauseClass);
    }

    public void verifyFailureCauseMessage(String expectedMessage) {
        assertThat(actualFailure().getCause()).hasMessage(expectedMessage);
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

    private VerificationBuilder<$Result> defaultResult() {
        return new VerificationBuilder<>(context::returnResultOrVoid);
    }

    private <$ActualResult> VerificationBuilder<$ActualResult> actualResult(Supplier<$ActualResult> resultSupplier) {
        return new VerificationBuilder<>(resultSupplier);
    }

    private Throwable actualFailure() {
        return (Throwable) context.returnResultOrVoid();
    }

    private class VerificationBuilder<$ActualResult> {

        private final Supplier<$ActualResult> resultSupplier;
        private Optional<Runnable> beforeVerification;
        private CheckedConsumer<$ActualResult> expectation;
        private String verificationErrorMessage;

        private VerificationBuilder(Supplier<$ActualResult> resultSupplier) {
            this.resultSupplier = resultSupplier;
            beforeVerification = Optional.empty();
        }

        VerificationBuilder<$ActualResult> toDoBefore(Runnable beforeVerification) {
            this.beforeVerification = Optional.of(beforeVerification);
            return this;
        }

        VerificationBuilder<$ActualResult> expectToSatisfy(CheckedConsumer<$ActualResult> expectation) {
            this.expectation = expectation;
            return this;
        }

        void orFail(String verificationErrorMessage) {
            this.verificationErrorMessage = verificationErrorMessage;
            verify();
        }

        private void verify() {
            beforeVerification.ifPresent(Runnable::run);
            context.returnResultOrVoid();
            $ActualResult result = resultSupplier.get();
            try {
                expectation.accept(result);
            } catch (AssertionError assertionError) {
                throw assertionError;
            } catch (Throwable throwable) {
                throw new VerificationError(verificationErrorMessage, throwable);
            }
        }
    }
}
