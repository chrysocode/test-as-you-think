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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedPredicate;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.verification.VerificationError;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static testasyouthink.TestAsYouThink.givenSutClass;

class ThenUnexpectedFailuresTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThenUnexpectedFailuresTest.class);

    @Nested
    class When_returning_nothing {

        @Nested
        class Then_running_a_verification_step {

            @Test
            void should_fail_to_run_a_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then((CheckedRunnable) () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_another_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then(() -> {})
                        .and((CheckedRunnable) () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_a_specified_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then("Expectations", () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_another_specified_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then("An expectation", () -> {})
                        .and("Another expectation", () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_consuming_the_sut {

            @Test
            void should_fail_to_verify_a_sut_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then(sut -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the expectations of the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_verify_another_sut_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then(sut -> {})
                        .and(sut -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the expectations of the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_verify_a_sut_specified_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then("Expectations", sut -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the expectations of the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_verify_another_sut_specified_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then("An expectation", sut -> {})
                        .and("Another expectation", sut -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the expectations of the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_supplying_a_boolean {

            @Test
            void should_fail_to_apply_a_condition() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then(() -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_apply_another_condition() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> {})
                        .then(() -> true)
                        .and(() -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_consuming_the_standard_output {

            @Test
            void should_fail_to_verify_a_stdout_expectation() {
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
    class When_returning_a_result {

        @Nested
        class Then_checking_predicates_on_both_the_result_and_the_sut {

            @Test
            void should_fail_to_check_result_and_sut_predicates_because_of_the_result() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(result -> {
                            throw new UnexpectedException();
                        }, sut -> true));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_check_result_and_sut_predicates_because_of_the_sut() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(result -> true, sut -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the expectations of the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_consuming_the_result {

            @Test
            void should_fail_to_verify_a_result_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then((CheckedConsumer<String>) result -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_verify_a_result_specified_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then("Expectations", result -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_verify_another_result_specified_expectation() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(result -> {})
                        .and("Expectations", result -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_running_a_verification_step {

            @Test
            void should_fail_to_run_a_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(() -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_another_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(() -> {})
                        .and(() -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_a_specified_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then("Expectations", () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_run_another_specified_verification_step() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then("Expectations", () -> {})
                        .and("Expectations", () -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class Then_checking_the_result_predicates {

            @Test
            void should_fail_to_check_a_result_predicate() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then((CheckedPredicate<String>) result -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_check_another_result_predicate() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(result -> true)
                        .and((CheckedPredicate<String>) result -> {
                            throw new UnexpectedException();
                        }));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_check_some_result_predicates() {
                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                        .when(sut -> "result")
                        .then(asList(result -> true, result -> {
                            throw new UnexpectedException();
                        }, result -> true)));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(VerificationError.class)
                        .hasMessage("Fails to verify the result expectations!")
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }
    }
}
