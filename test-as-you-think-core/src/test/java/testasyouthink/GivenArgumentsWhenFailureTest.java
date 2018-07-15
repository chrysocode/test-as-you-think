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

import org.easymock.IMocksControl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.execution.ExecutionError;
import testasyouthink.fixture.ExpectedException;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.fixture.Specifications.ExpectedMessage.EXPECTED_EXECUTION_FAILURE_MESSAGE;

class GivenArgumentsWhenFailureTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenArgumentsWhenFailureTest.class);
    private static final String GIVEN_STRING = "given argument";
    private static final int GIVEN_INTEGER = 201705;
    private static final boolean GIVEN_BOOLEAN = false;
    private static final String EXPECTED_ERROR_MESSAGE = "Expected error message";

    private IMocksControl mocksControl;
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @BeforeEach
    void prepareFixtures() {
        // GIVEN
        mocksControl = createStrictControl();
        systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
    }

    @AfterEach
    void verifyMocks() {
        // THEN
        mocksControl.verify();
    }

    @Nested
    class When_having_one_parameter {

        @Nested
        class When_an_unexpected_failure_happens {

            @Test
            void should_fail_to_execute_a_void_target_method_with_one_parameter() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                systemUnderTestMock.failWithParameter(GIVEN_STRING);
                expectLastCall().andThrow(new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .when(SystemUnderTest::failWithParameter)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_execute_a_non_void_target_method_with_one_parameter() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expect(systemUnderTestMock.nonVoidFailWithParameter(GIVEN_STRING)).andThrow(new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .when(SystemUnderTest::nonVoidFailWithParameter)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class When_an_expected_failure_happens {

            @Test
            void should_verify_the_sut_fails_given_one_method_parameter() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                systemUnderTestMock.failWithParameter(GIVEN_STRING);
                expectLastCall().andThrow(new ExpectedException(EXPECTED_ERROR_MESSAGE));
                mocksControl.replay();

                // WHEN
                givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithParameter)
                        .thenItFails()
                        .becauseOf(ExpectedException.class)
                        .withMessage(EXPECTED_ERROR_MESSAGE);
            }
        }
    }

    @Nested
    class When_having_two_parameters {

        @Nested
        class When_an_unexpected_failure_happens {

            @Test
            void should_fail_to_execute_a_void_target_method_with_two_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(2);
                systemUnderTestMock.failWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
                expectLastCall().andThrow(new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .when(SystemUnderTest::failWithTwoParameters)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_execute_a_non_void_target_method_with_two_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(2);
                expect(systemUnderTestMock.nonVoidFailWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER)).andThrow(
                        new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .when(SystemUnderTest::nonVoidFailWithTwoParameters)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class When_an_expected_failure_happens {

            @Test
            void should_verify_the_sut_fails_given_two_method_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(2);
                systemUnderTestMock.failWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
                expectLastCall().andThrow(new ExpectedException(EXPECTED_ERROR_MESSAGE));
                mocksControl.replay();

                // WHEN
                givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithTwoParameters)
                        .thenItFails()
                        .becauseOf(ExpectedException.class)
                        .withMessage(EXPECTED_ERROR_MESSAGE);
            }
        }
    }

    @Nested
    class When_having_three_parameters {

        @Nested
        class When_an_unexpected_failure_happens {

            @Test
            void should_fail_to_execute_a_void_taget_method_with_three_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(3);
                systemUnderTestMock.failWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
                expectLastCall().andThrow(new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_BOOLEAN;
                        })
                        .when(SystemUnderTest::failWithThreeParameters)
                        .then(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult
                                ()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }

            @Test
            void should_fail_to_execute_a_non_void_target_method_with_three_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(3);
                expect(systemUnderTestMock.nonVoidFailWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER,
                        GIVEN_BOOLEAN)).andThrow(new UnexpectedException());
                mocksControl.replay();

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_BOOLEAN;
                        })
                        .when(SystemUnderTest::nonVoidFailWithThreeParameters)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(ExecutionError.class)
                        .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                        .hasCauseInstanceOf(UnexpectedException.class);
            }
        }

        @Nested
        class When_an_expected_failure_happens {

            @Test
            void should_verify_the_sut_fails_given_three_method_parameters() throws Throwable {
                // GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(3);
                systemUnderTestMock.failWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
                expectLastCall().andThrow(new ExpectedException(EXPECTED_ERROR_MESSAGE));
                mocksControl.replay();

                // WHEN
                givenSut(systemUnderTestMock)
                        .givenArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_STRING;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_INTEGER;
                        })
                        .andArgument(() -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            return GIVEN_BOOLEAN;
                        })
                        .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithThreeParameters)
                        .thenItFails()
                        .becauseOf(ExpectedException.class)
                        .withMessage(EXPECTED_ERROR_MESSAGE);
            }
        }
    }
}
