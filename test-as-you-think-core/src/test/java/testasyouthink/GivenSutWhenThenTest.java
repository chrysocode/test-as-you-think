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

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

class GivenSutWhenThenTest {

    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @AfterEach
    void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
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
            }

            @Test
            void should_receive_a_sut_instance() {
                // GIVEN
                givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);

                // WHEN
                givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                        .when(SystemUnderTest::voidMethod)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
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
                                givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                                assertThat(result).isEqualTo(EXPECTED_RESULT);
                            });
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
                }
            }
        }
    }
}
