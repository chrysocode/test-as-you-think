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

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.preparation.PreparationError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static testasyouthink.SutPreparationAssertions.assertThatFailure;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

class GivenSutWhenThenTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenSutWhenThenTest.class);
    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    public static class SystemUnderTestFailingToBeInstantiated {

        public SystemUnderTestFailingToBeInstantiated() throws Exception {
            throw new UnexpectedException("Impossible to instantiate it!");
        }

        void voidMethod() {}
    }

    @Nested
    class Given_a_SUT_as_an_instance {

        @Nested
        class When_returning_nothing {

            @Test
            void should_instantiate_and_prepare_the_sut() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps();

                // WHEN
                givenSut(() -> {
                    SystemUnderTest systemUnderTest = new SystemUnderTest(givenWhenThenDefinitionMock);
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return systemUnderTest;
                })
                        .when(SystemUnderTest::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

                // THEN
                verify(givenWhenThenDefinitionMock);
            }

            @Test
            void should_receive_a_sut_instance() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);

                // WHEN
                givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                        .when(SystemUnderTest::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

                // THEN
                verify(givenWhenThenDefinitionMock);
            }

            @Test
            void should_receive_a_sut_instance_and_prepare_some_fixture() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps();

                // WHEN
                givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                        .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                        .when(SystemUnderTest::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

                // THEN
                verify(givenWhenThenDefinitionMock);
            }
        }

        @Nested
        class When_returning_a_result {

            @Test
            void should_receive_a_sut_instance() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);

                // WHEN
                givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isEqualTo(EXPECTED_RESULT);
                        });

                // THEN
                verify(givenWhenThenDefinitionMock);
            }

            @Test
            void should_receive_a_sut_instance_and_prepare_some_fixture() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps();

                // WHEN
                givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                        .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                        .when(SystemUnderTest::nonVoidMethod)
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isEqualTo(EXPECTED_RESULT);
                        });

                // THEN
                verify(givenWhenThenDefinitionMock);
            }
        }

        @Nested
        class Failing_to_prepare_the_SUT {

            @Test
            void should_fail_to_supply_a_sut_instance() {
                // GIVEN
                givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSut(() -> new SystemUnderTestFailingToBeInstantiated())
                        .when(SystemUnderTestFailingToBeInstantiated::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                assertThatFailure(thrown).happensWhilePreparingSut();
                verifyZeroInteractions(givenWhenThenDefinitionMock);
            }
        }
    }

    @Nested
    class Given_a_SUT_as_a_class_to_be_instantiated {

        @Nested
        class When_returning_a_result {

            @Test
            void should_instantiate_the_sut_and_verify_the_result() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);

                // WHEN
                givenSutClass(SystemUnderTest.class)
                        .when(sut -> {
                            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                            return sut.nonVoidMethod();
                        })
                        .then(result -> {
                            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            assertThat(result).isEqualTo(EXPECTED_RESULT);
                        });

                // THEN
                verify(givenWhenThenDefinitionMock);
            }
        }

        @Nested
        class Failing_to_instantiate_the_SUT {

            @Test
            void should_fail_to_create_a_sut_instance() {
                // GIVEN
                givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                // WHEN
                Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTestFailingToBeInstantiated.class)
                        .when(SystemUnderTestFailingToBeInstantiated::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

                // THEN
                LOGGER.debug("Stack trace", thrown);
                assertThat(thrown)
                        .isInstanceOf(PreparationError.class)
                        .hasMessage("Fails to instantiate the system under test!")
                        .hasCauseInstanceOf(UnexpectedException.class);
                verifyZeroInteractions(givenWhenThenDefinitionMock);
            }
        }

        @Nested
        class Given_a_SUT_to_be_prepared {

            @Nested
            class When_returning_a_result {

                @Test
                void should_prepare_the_sut_in_a_separated_preparation_step_and_verify_the_result() {
                    // GIVEN
                    givenWhenThenDefinitionMock = orderedSteps();

                    // WHEN
                    givenSutClass(SystemUnderTest.class)
                            .given(sut -> {
                                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                                sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                            })
                            .when(SystemUnderTest::nonVoidMethod)
                            .then(result -> {
                                assertThat(result).isEqualTo(EXPECTED_RESULT);
                                givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            });

                    // THEN
                    verify(givenWhenThenDefinitionMock);
                }

                @Test
                void should_prepare_the_sut_in_a_separated_preparation_specified_step_and_verify_the_result() {
                    // GIVEN
                    givenWhenThenDefinitionMock = orderedSteps();

                    // WHEN
                    givenSutClass(SystemUnderTest.class)
                            .given("what it makes this fixture specific to the current use case", sut -> {
                                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                                sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                            })
                            .when(SystemUnderTest::nonVoidMethod)
                            .then(result -> {
                                assertThat(result).isEqualTo(EXPECTED_RESULT);
                                givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                            });

                    // THEN
                    verify(givenWhenThenDefinitionMock);
                }
            }

            @Nested
            class When_returning_nothing {

                @Test
                void should_prepare_the_sut_in_a_separated_preparation_step_and_verify_the_sut_state() {
                    // GIVEN
                    givenWhenThenDefinitionMock = orderedSteps();

                    // WHEN
                    givenSutClass(SystemUnderTest.class)
                            .given(sut -> {
                                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                                sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                            })
                            .when(SystemUnderTest::voidMethod)
                            .then(sut -> {
                                givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                                assertThat(sut.getState()).isNotNull();
                            });

                    // THEN
                    verify(givenWhenThenDefinitionMock);
                }

                @Test
                void should_instantiate_and_prepare_the_sut_in_the_same_preparation_step() {
                    // GIVEN
                    givenWhenThenDefinitionMock = orderedSteps();

                    // WHEN
                    givenSut(SystemUnderTest.class, sut -> {
                        sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    })
                            .when(SystemUnderTest::voidMethod)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult());

                    // THEN
                    verify(givenWhenThenDefinitionMock);
                }
            }

            @Nested
            class Failing_to_prepare_the_SUT {

                @Test
                void should_fail_to_prepare_the_sut() {
                    // GIVEN
                    givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .given(sut -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethod)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingSut();
                    verifyZeroInteractions(givenWhenThenDefinitionMock);
                }

                @Test
                void should_fail_to_prepare_the_sut_after_instantiating_it() {
                    // GIVEN
                    givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(SystemUnderTest.class, sut -> {
                        throw new UnexpectedException();
                    })
                            .when(SystemUnderTest::voidMethod)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingSut();
                    verifyZeroInteractions(givenWhenThenDefinitionMock);
                }

                @Test
                void should_fail_to_prepare_the_sut_with_its_specification() {
                    // GIVEN
                    givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .given("SUT specification", sut -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethod)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingSut();
                    verifyZeroInteractions(givenWhenThenDefinitionMock);
                }

                @Test
                void should_fail_to_prepare_the_sut_with_its_specifications() {
                    // GIVEN
                    givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .given("SUT specification that passes", sut -> {})
                            .and("SUT specification that fails", sut -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethod)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingSut();
                    verifyZeroInteractions(givenWhenThenDefinitionMock);
                }
            }
        }
    }
}

class SutPreparationAssertions extends AbstractAssert<SutPreparationAssertions, Throwable> {

    private static Logger LOGGER = LoggerFactory.getLogger(SutPreparationAssertions.class);

    private SutPreparationAssertions(Throwable actual) {
        super(actual, SutPreparationAssertions.class);
    }

    static SutPreparationAssertions assertThatFailure(Throwable actual) {
        return new SutPreparationAssertions(actual);
    }

    void happensWhilePreparingSut() {
        LOGGER.debug("Stack trace", actual);
        assertThat(actual)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(UnexpectedException.class);
    }
}