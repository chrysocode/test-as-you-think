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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.verification.VerificationError;

import java.util.List;
import java.util.concurrent.CountDownLatch;
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

/**
 * Acceptance testing for the verification step, only to verify the standard streams.
 */
class ThenStandardStreamsAsResultTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThenStandardStreamsAsResultTest.class);

    @Nested
    class When_returning_nothing {

        @Nested
        class Then_verifying_the_stdout {

            @Test
            void should_verify_the_stdout() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.out.println("Stdout as a result");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardOutput(stdout -> {
                            assertThat(stdout).hasContent("Stdout as a result");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock)
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stdout_in_2_times() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.out.println("Stdout as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardOutput(stdout -> {
                            assertThat(linesOf(stdout)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardOutput(stdout -> {
                            assertThat(stdout).hasContent("Stdout as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stdout_in_2_times_by_specifying_expectations() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.out.println("Stdout as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardOutput("number of lines", stdout -> {
                            assertThat(linesOf(stdout)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardOutput("content", stdout -> {
                            assertThat(stdout).hasContent("Stdout as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Nested
            class Then_failing_to_verify_stdout {

                @Test
                void should_fail_to_verify_the_stdout_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardOutput(result -> fail("Stdout non-compliant")));

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
                            .thenStandardOutput(stdout -> {
                                throw new UnexpectedException();
                            }));

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
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.err.println("Standard error stream as a result");
                            System.out.println("Standard output stream");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardError(stderr -> {
                            assertThat(stderr).hasContent("Standard error stream as a result");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock)
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stderr_in_2_times() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.err.println("Stderr as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardError(stderr -> {
                            assertThat(linesOf(stderr)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardError(stderr -> {
                            assertThat(stderr).hasContent("Stderr as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stderr_in_2_times_by_specifying_expectations() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.err.println("Stderr as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardError("number of lines", stderr -> {
                            assertThat(linesOf(stderr)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardError("content", stderr -> {
                            assertThat(stderr).hasContent("Stderr as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Nested
            class Then_failing_to_verify_stderr {

                @Test
                void should_fail_to_verify_the_stderr_content() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .thenStandardError(result -> fail("Stderr non-compliant")));

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
                            .thenStandardError(stderr -> {
                                throw new UnexpectedException();
                            }));

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
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.out.println("Stdout as a result");
                            System.err.println("Stderr as a result");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardStreams(stdstr -> {
                            assertThat(stdstr).hasContent("Stdout as a result\n" //
                                    + "Stderr as a result");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .and(gwtMock::thenTheActualResultIsInKeepingWithTheExpectedResult);

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_standard_streams_in_2_times() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.out.println("Stdout as a result");
                            System.err.println("Stderr as a result");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        })
                        .thenStandardStreams(stdstr -> {
                            assertThat(linesOf(stdstr)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardStreams(stdstr -> {
                            assertThat(stdstr).hasContent("Stdout as a result\n" //
                                    + "Stderr as a result");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
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
                                    System.out.println("Stdout as a result of thread #" + count);
                                })
                                .thenStandardOutput(stdout -> {
                                    softly
                                            .assertThat(stdout)
                                            .hasContent("Stdout as a result of thread #" + count);
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

        @Nested
        class Then_verifying_the_stdout {

            @Test
            void should_verify_the_stdout_and_the_result() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            System.out.println("Stdout as a result");
                            return "expected result";
                        })
                        .thenStandardOutput(stdout -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(stdout).hasContent("Stdout as a result");
                        })
                        .and(result -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isEqualTo("expected result");
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stdout_in_2_times_by_specifying_expectations() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            System.out.println("Stdout as a result\nwith 2 lines");
                            return "expected result";
                        })
                        .thenStandardOutput("number of lines", stdout -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(linesOf(stdout)).hasSize(2);
                        })
                        .andStandardOutput("content", stdout -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(stdout).hasContent("Stdout as a result\nwith 2 lines");
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stdout_in_2_times() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            System.out.println("Stdout as a result\nwith 2 lines");
                            return "expected result";
                        })
                        .thenStandardOutput(stdout -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(linesOf(stdout)).hasSize(2);
                        })
                        .andStandardOutput(stdout -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(stdout).hasContent("Stdout as a result\nwith 2 lines");
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }
        }

        @Nested
        class Then_verifying_the_stderr {

            @Test
            void should_verify_the_stderr_and_the_result() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            System.err.println("Stderr as a result");
                            return "expected result";
                        })
                        .thenStandardError(stderr -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(stderr).hasContent("Stderr as a result");
                        })
                        .and(result -> {
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isEqualTo("expected result");
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stderr_in_2_times() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.err.println("Stderr as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            return "expected result";
                        })
                        .thenStandardError(stderr -> {
                            assertThat(linesOf(stderr)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardError(stderr -> {
                            assertThat(stderr).hasContent("Stderr as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }

            @Test
            void should_verify_the_stderr_in_2_times_by_specifying_expectations() {
                // GIVEN
                GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            System.err.println("Stderr as a result\nwith 2 lines");
                            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            return "expected result";
                        })
                        .thenStandardError("number of lines", stderr -> {
                            assertThat(linesOf(stderr)).hasSize(2);
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .andStandardError("content", stderr -> {
                            assertThat(stderr).hasContent("Stderr as a result\nwith 2 lines");
                            gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });

                // THEN
                InOrder inOrder = inOrder(gwtMock);
                inOrder
                        .verify(gwtMock)
                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                inOrder
                        .verify(gwtMock, times(2))
                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                inOrder.verifyNoMoreInteractions();
            }
        }
    }
}
