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
import testasyouthink.function.CheckedConsumer;

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
        class Then_verifying_assertions_on_the_result {

            @Test
            void should_consume_result_assertions_separately() {
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
            void should_consume_a_specified_a_result_assertion() {
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
            void should_consume_specified_result_assertions_separately() {
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
            void should_consume_assertions_on_both_the_result_and_the_sut() {
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

            @Nested
            class Then_failing_to_consume_assertions_on_the_result {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_consume_a_result_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then((CheckedConsumer<String>) result -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_another_result_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> {})
                            .and((CheckedConsumer<String>) result -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_a_specified_result_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then("Expectations", result -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_another_specified_result_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> {})
                            .and("Expectations", result -> assertThat(true).isFalse()));
                }
            }
        }

        @Nested
        class Then_verifying_predicates {

            @Test
            void should_verify_result_predicates_separately() {
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
            void should_verify_a_predicate_list_on_the_result() {
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
            class Then_failing_to_verify_a_predicate {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_verify_a_result_predicate() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> false));
                }

                @Test
                void should_fail_to_verify_another_result_predicate() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> true)
                            .and(result -> false));
                }

                @Test
                void should_fail_to_verify_a_predicate_list_on_the_result() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(asList(result -> true, result -> false)));
                }

                @Test
                void should_fail_to_verify_predicates_on_both_the_result_and_the_SUT_because_of_the_result() {

                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> false, sut -> true));
                }

                @Test
                void should_fail_to_verify_predicates_on_both_the_result_and_the_SUT_because_of_the_SUT() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(result -> true, sut -> false));
                }
            }
        }

        @Nested
        class Then_verifying_runnable_expectations {

            @Nested
            class Then_failing_to_verify_runnable_expectations {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_run_an_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(() -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_another_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then(() -> {})
                            .and(() -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_a_specified_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then("Expectations", () -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_another_specified_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> "result")
                            .then("Expectations", () -> {})
                            .and("Expectations", () -> assertThat(true).isFalse()));
                }
            }
        }
    }

    @Nested
    class When_returning_nothing {

        @Nested
        class Then_verifying_runnable_expectations {

            @Test
            void should_run_expectations_separately() {
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
            void should_run_a_specified_expectation() {
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
            void should_run_specified_expectations_separately() {
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

            @Nested
            class Then_failing_to_verify_runnable_expectations {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_run_an_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(() -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_another_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(() -> {})
                            .and(() -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_a_specified_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then("Expectations", () -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_run_another_specified_expectation() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then("An expectation", () -> {})
                            .and("Another expectation", () -> assertThat(true).isFalse()));
                }
            }
        }

        @Nested
        class Then_verifying_assertions_on_the_SUT {

            @Test
            void should_consume_sut_assertions_separately() {
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
            void should_consume_specified_sut_assertions_separately() {
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

            @Nested
            class Failing_to_verify_the_SUT {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_consume_a_sut_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(sut -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_another_sut_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(sut -> {})
                            .and(sut -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_a_specified_sut_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then("Expectations", sut -> assertThat(true).isFalse()));
                }

                @Test
                void should_fail_to_consume_another_specified_sut_assertion() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then("An expectation", sut -> {})
                            .and("Another expectation", sut -> assertThat(true).isFalse()));
                }
            }
        }

        @Nested
        class Then_verifying_boolean_suppliers {

            @Test
            void should_supply_true() {
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

            @Nested
            class Then_failing_to_verify_boolean_suppliers {

                private Throwable thrown;

                @AfterEach
                void verifyError() {
                    // THEN
                    assertThat(thrown)
                            .isInstanceOf(AssertionError.class)
                            .hasNoCause();
                }

                @Test
                void should_fail_to_supply_true() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(() -> false));
                }

                @Test
                void should_fail_to_supply_another_true() {
                    // WHEN
                    thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .when(sut -> {})
                            .then(() -> true)
                            .and(() -> false));
                }
            }
        }
    }
}
