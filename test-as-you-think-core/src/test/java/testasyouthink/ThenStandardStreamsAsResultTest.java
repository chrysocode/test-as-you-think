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

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.verification.VerificationError;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.linesOf;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.EXPECTED_RESULT;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.PRINTED_ON_BOTH;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.PRINTED_ON_STDERR;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.PRINTED_ON_STDERR_WITH_2_LINES;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.PRINTED_ON_STDOUT;
import static testasyouthink.ThenStandardStreamsAsResultTest.GivenData.PRINTED_ON_STDOUT_WITH_2_LINES;

/**
 * Acceptance testing for the verification step, only to verify the standard streams.
 */
class ThenStandardStreamsAsResultTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThenStandardStreamsAsResultTest.class);
    private GivenWhenThenDefinition gwtMock;
    private Steps steps;

    @BeforeEach
    void prepareFixtures() {
        gwtMock = mock(GivenWhenThenDefinition.class);
        steps = new Steps();
    }

    void whenOnceThenTwice() {
        InOrder inOrder = inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock, times(2))
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }

    static class GivenData {

        static final String PRINTED_ON_STDOUT = "Printed on stdout";
        static final String PRINTED_ON_STDERR = "Printed on stderr";
        static final String PRINTED_ON_STDOUT_WITH_2_LINES = "Printed on stdout\nwith 2 lines";
        static final String PRINTED_ON_STDERR_WITH_2_LINES = "Printed on stderr\nwith 2 lines";
        static final String PRINTED_ON_BOTH = PRINTED_ON_STDOUT + "\n" + PRINTED_ON_STDERR;
        static final String EXPECTED_RESULT = "expected result";
    }

    class Steps {

        final Function<String, CheckedConsumer<File>> succeedingAtVerifyingStream = printed -> file -> {
            assertThat(file).hasContent(printed);
            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        };

        final CheckedConsumer<File> succeedingAtVerifyingNumberOfLines = stdout -> {
            assertThat(linesOf(stdout)).hasSize(2);
            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        };

        final CheckedConsumer<File> failingBecauseOfAssertion = file -> fail("Standard stream non-compliant");

        final CheckedConsumer<File> failingBecauseOfUnexpectedException = file -> {
            throw new UnexpectedException();
        };
    }

    @Nested
    class When_returning_nothing {

        final Function<String, CheckedConsumer<SystemUnderTest>> printingOnStdout = toPrintOnStdout -> sut -> {
            System.out.println(toPrintOnStdout);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        };

        final Function<String, CheckedConsumer<SystemUnderTest>> printingOnStderr = toPrintOnStderr -> sut -> {
            System.err.println(toPrintOnStderr);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        };

        final CheckedConsumer<SystemUnderTest> printingOnBoth = sut -> {
            System.out.println(PRINTED_ON_STDOUT);
            System.err.println(PRINTED_ON_STDERR);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        };

        @Nested
        class Then_verifying_the_stdout {

            @Test
            void should_verify_the_stdout() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT))
                        .thenStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT))
                        .and(gwtMock::thenTheActualResultIsInKeepingWithTheExpectedResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stdout_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT_WITH_2_LINES))
                        .thenStandardOutput(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stdout_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT_WITH_2_LINES))
                        .thenStandardOutput("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardOutput("content", stdout -> {
                            assertThat(stdout).hasContent(PRINTED_ON_STDOUT_WITH_2_LINES);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                whenOnceThenTwice();
            }

            @Nested
            class Then_failing_to_verify_stdout {

                @Test
                void should_fail_to_verify_the_stdout_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardOutput(steps.failingBecauseOfAssertion));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }
            }

            @Nested
            class Then_failing_because_of_an_unexpected_failure {

                @Test
                void should_fail_to_verify_the_stdout_expectation() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardOutput(steps.failingBecauseOfUnexpectedException));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(VerificationError.class)
                            .hasMessage("Fails to verify the expectations of the stdout!")
                            .hasCauseInstanceOf(UnexpectedException.class);
                }
            }
        }

        @Nested
        class Then_verifying_the_stderr {

            @Test
            void should_verify_the_stderr() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR))
                        .thenStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR))
                        .and(gwtMock::thenTheActualResultIsInKeepingWithTheExpectedResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stderr_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR_WITH_2_LINES))
                        .thenStandardError(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stderr_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR_WITH_2_LINES))
                        .thenStandardError("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardError("content",
                                steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }

            @Nested
            class Then_failing_to_verify_stderr {

                @Test
                void should_fail_to_verify_the_stderr_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardError(steps.failingBecauseOfAssertion));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }
            }

            @Nested
            class Then_failing_because_of_an_unexpected_failure {

                @Test
                void should_fail_to_verify_the_stderr_expectation() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardError(steps.failingBecauseOfUnexpectedException));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(VerificationError.class)
                            .hasMessage("Fails to verify the expectations of the stderr!")
                            .hasCauseInstanceOf(UnexpectedException.class);
                }
            }
        }

        @Nested
        class Then_verifying_standard_streams_together {

            @Test
            void should_verify_standard_streams() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH))
                        .and(gwtMock::thenTheActualResultIsInKeepingWithTheExpectedResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_standard_streams_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardStreams(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_standard_streams_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardStreams("content", steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH));

                // THEN
                whenOnceThenTwice();
            }

            @Nested
            class Then_failing_to_verify_standard_streams {

                @Test
                void should_fail_to_verify_the_standard_streams_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardStreams(steps.failingBecauseOfAssertion));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }
            }

            @Nested
            class Then_failing_because_of_an_unexpected_failure {

                @Test
                void should_fail_to_verify_the_standard_streams_expectation() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardStreams(steps.failingBecauseOfUnexpectedException));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(VerificationError.class)
                            .hasMessage("Fails to verify the expectations of the standard streams!")
                            .hasCauseInstanceOf(UnexpectedException.class);
                }
            }
        }

        @Nested
        class Then_verifying_standard_stream_separately {

            @Test
            void should_verify_stdout_then_stderr() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT))
                        .andStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_stderr_then_stdout() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR))
                        .andStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT));

                // THEN
                whenOnceThenTwice();
            }
        }

        @Nested
        class Given_multiple_threads {

            @Test
            void should_verify_the_stdout_for_multiple_threads() throws InterruptedException {
                // GIVEN
                final int numberOfThreads = 10;
                List<GivenWhenThenDefinition> gwtMocks = IntStream
                        .range(0, numberOfThreads)
                        .mapToObj(count -> mock(GivenWhenThenDefinition.class))
                        .collect(toList());
                CountDownLatch counterOfThreadsToPrepare = new CountDownLatch(numberOfThreads);
                CountDownLatch callingThreadBlocker = new CountDownLatch(1);
                CountDownLatch counterOfThreadsToComplete = new CountDownLatch(numberOfThreads);
                SoftAssertions softly = new SoftAssertions();

                // WHEN
                IntStream
                        .range(0, numberOfThreads)
                        .mapToObj(count -> new Thread(() -> givenSutClass(SystemUnderTest.class)
                                .when(sut -> {
                                    counterOfThreadsToPrepare.countDown();
                                    callingThreadBlocker.await();
                                    gwtMocks
                                            .get(count)
                                            .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                                    System.out.println(PRINTED_ON_STDOUT + " by thread #" + count);
                                })
                                .thenStandardOutput(stdout -> {
                                    softly
                                            .assertThat(stdout)
                                            .hasContent(PRINTED_ON_STDOUT + " by thread #" + count);
                                    gwtMocks
                                            .get(count)
                                            .thenTheActualResultIsInKeepingWithTheExpectedResult();
                                    counterOfThreadsToComplete.countDown();
                                })))
                        .forEach(Thread::start);
                counterOfThreadsToPrepare.await();
                LOGGER.debug("All threads ready!");
                LOGGER.debug("Writing in console can start...");
                callingThreadBlocker.countDown();
                counterOfThreadsToComplete.await();
                LOGGER.debug("All threads complete!");

                // THEN
                gwtMocks.forEach(gwt -> {
                    InOrder inOrder = inOrder(gwt);
                    inOrder
                            .verify(gwt)
                            .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    inOrder
                            .verify(gwt)
                            .thenTheActualResultIsInKeepingWithTheExpectedResult();
                    inOrder.verifyNoMoreInteractions();
                });
                softly.assertAll();
            }
        }
    }

    @Nested
    class When_returning_a_result {

        final Function<String, CheckedFunction<SystemUnderTest, String>> printingOnStdout = toPrintOnStdout -> sut -> {
            System.out.println(toPrintOnStdout);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return EXPECTED_RESULT;
        };

        final Function<String, CheckedFunction<SystemUnderTest, String>> printingOnStderr = toPrintOnStderr -> sut -> {
            System.err.println(toPrintOnStderr);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return EXPECTED_RESULT;
        };

        final CheckedFunction<SystemUnderTest, String> printingOnBoth = sut -> {
            System.out.println(PRINTED_ON_STDOUT);
            System.err.println(PRINTED_ON_STDERR);
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return EXPECTED_RESULT;
        };

        CheckedConsumer<String> succeedingAtVerifyingResult = result -> {
            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            assertThat(result).isEqualTo(EXPECTED_RESULT);
        };

        @Nested
        class Then_verifying_the_stdout {

            @Test
            void should_verify_the_stdout_and_the_result() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT))
                        .thenStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT))
                        .and(succeedingAtVerifyingResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stdout_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT_WITH_2_LINES))
                        .thenStandardOutput("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardOutput("content",
                                steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stdout_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStdout.apply(PRINTED_ON_STDOUT_WITH_2_LINES))
                        .thenStandardOutput(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }
        }

        @Nested
        class Then_verifying_the_stderr {

            @Test
            void should_verify_the_stderr_and_the_result() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR))
                        .thenStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR))
                        .and(succeedingAtVerifyingResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stderr_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR_WITH_2_LINES))
                        .thenStandardError(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_the_stderr_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnStderr.apply(PRINTED_ON_STDERR_WITH_2_LINES))
                        .thenStandardError("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardError("content",
                                steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR_WITH_2_LINES));

                // THEN
                whenOnceThenTwice();
            }
        }

        @Nested
        class Then_verifying_standard_streams_together {

            @Test
            void should_verify_standard_streams() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH))
                        .and(succeedingAtVerifyingResult);

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_standard_streams_in_2_times() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams(steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardStreams(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_standard_streams_in_2_times_by_specifying_expectations() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardStreams("number of lines", steps.succeedingAtVerifyingNumberOfLines)
                        .andStandardStreams("content", steps.succeedingAtVerifyingStream.apply(PRINTED_ON_BOTH));

                // THEN
                whenOnceThenTwice();
            }

            @Nested
            class Then_failing_to_verify_standard_streams {

                @Test
                void should_fail_to_verify_the_standard_streams_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> EXPECTED_RESULT)
                            .thenStandardStreams(steps.failingBecauseOfAssertion));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }
            }

            @Nested
            class Then_failing_because_of_an_unexpected_failure {

                @Test
                void should_fail_to_verify_the_standard_streams_expectation() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> EXPECTED_RESULT)
                            .thenStandardStreams(steps.failingBecauseOfUnexpectedException));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(VerificationError.class)
                            .hasMessage("Fails to verify the expectations of the standard streams!")
                            .hasCauseInstanceOf(UnexpectedException.class);
                }
            }
        }

        @Nested
        class Then_verifying_standard_stream_separately {

            @Test
            void should_verify_stdout_then_stderr() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT))
                        .andStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR));

                // THEN
                whenOnceThenTwice();
            }

            @Test
            void should_verify_stderr_then_stdout() {
                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(printingOnBoth)
                        .thenStandardError(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDERR))
                        .andStandardOutput(steps.succeedingAtVerifyingStream.apply(PRINTED_ON_STDOUT));

                // THEN
                whenOnceThenTwice();
            }
        }
    }
}
