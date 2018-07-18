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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.verify;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

class ThenExpectationsTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @AfterEach
    void verifyMocks() {
        // THEN
        if (givenWhenThenDefinitionMock != null) {
            verify(givenWhenThenDefinitionMock);
        }
    }

    @Nested
    class When_returning_a_result {

        @Nested
        class Then_verifying_assertions {

            @Test
            void should_verify_result_assertions_separately() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            return sut.nonVoidMethod();
                        })
                        .then(result -> {
                            assertThat(result).startsWith("expected");
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .and(result -> {
                            assertThat(result).contains(" ");
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        })
                        .and(result -> {
                            assertThat(result).endsWith("result");
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });
            }

            @Test
            void should_specify_a_result_assertion() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 1);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then("what the focus of this expectation is", result -> {
                            assertThat(result).isEqualTo("expected result");
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        });
            }

            @Test
            void should_specify_separated_result_assertions() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then("what the focus of this expectation is", result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).contains("expected");
                        })
                        .and("what the focus of this expectation is", result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).contains("result");
                        })
                        .and("what the focus of this expectation is",
                                () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult
                                        ());
            }

            @Test
            void should_verify_assertions_on_both_the_result_and_the_sut() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 2);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isInstanceOf(String.class);
                        }, sut -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(sut).isInstanceOf(SystemUnderTest.class);
                        });
            }
        }

        @Nested
        class Then_verifying_predicates {

            @Test
            void should_verify_result_predicates() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        })
                        .and(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        })
                        .and(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        });
            }

            @Test
            void should_verify_predicates_on_both_the_result() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 2);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(asList(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        }, result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        }));
            }

            @Test
            void should_verify_predicates_on_both_the_system_and_the_result() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 2);

                // THEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        }, sut -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        });
            }

            @Nested
            class Failing_to_verify_a_predicate {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_get_an_assertion_error_from_a_result_predicate() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> false));
                }

                @Test
                void should_get_an_assertion_error_from_another_result_predicate() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> true)
                            .and(result -> false));
                }

                @Test
                void should_get_an_assertion_error_from_result_predicates() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(asList(result -> true, result -> false)));
                }
            }
        }
    }

    @Nested
    class When_returning_nothing {

        @Nested
        class Then_verifying_expectations {

            @Test
            void should_verify_ordinary_expectations_separately() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            sut.voidMethod();
                        })
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
            }

            @Test
            void should_specify_an_expectation() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 1);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::nonVoidMethod)
                        .then("what the focus of this expectation is",
                                () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult
                                        ());
            }

            @Test
            void should_specify_separated_expectations() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::voidMethod)
                        .then("what the focus of this expectation is",
                                () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and("what the focus of this other expectation is",
                                () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and("what the focus of this other expectation is",
                                () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult
                                        ());
            }

            @Test
            void should_verify_sut_assertions_separately() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            sut.voidMethod();
                        })
                        .then(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
            }

            @Test
            void should_specify_separated_sut_assertions() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 0, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(SystemUnderTest::voidMethod)
                        .then("what the focus of this expectation is",
                                sut -> givenWhenThenDefinitionMock
                                        .thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and("what the focus of this other expectation is",
                                sut -> givenWhenThenDefinitionMock
                                        .thenTheActualResultIsInKeepingWithTheExpectedResult())
                        .and("what the focus of this other expectation is",
                                sut -> givenWhenThenDefinitionMock
                                        .thenTheActualResultIsInKeepingWithTheExpectedResult());
            }
        }

        @Nested
        class Then_verifying_predicates {

            @Test
            void should_verify_predicates() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(1, 3);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .given(sut -> {
                            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                            sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        })
                        .when(SystemUnderTest::voidMethod)
                        .then(() -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        })
                        .and(() -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        })
                        .and(() -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            return true;
                        });
            }
        }
    }
}
