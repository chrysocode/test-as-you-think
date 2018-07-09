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

class GivenWhenThenTest {

    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @AfterEach
    void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    void should_verify_expectations_on_both_the_result_and_the_sut_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
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
    class Given_a_SUT_as_an_instance {

        @Test
        void should_follow_the_when_then_partial_sequence_given_a_void_method() {
            // GIVEN
            givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);

            // WHEN
            givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                    .when(SystemUnderTest::voidMethod)
                    .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
        }

        @Test
        void should_follow_the_when_then_partial_sequence_given_a_non_void_method() {
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
        void should_follow_the_given_when_then_full_sequence_given_a_non_void_method() {
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

        @Test
        void should_follow_the_given_when_then_full_sequence_given_a_void_method() {
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
    class Given_a_SUT_as_a_class {

        @Test
        void should_follow_the_when_then_partial_sequence_given_a_sut_class_to_be_instantiated() {
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

        @Test
        void should_follow_the_given_when_then_full_sequence_given_a_sut_class_to_be_instantiated() {
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

        @Test
        void should_verify_expectations_on_the_sut_given_a_void_method() {
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
        void should_prepare_the_sut_with_a_given_step() {
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
        void should_prepare_the_sut_with_a_given_step_given_a_sut_class_to_be_instantiated() {
            // GIVEN
            givenWhenThenDefinitionMock = orderedSteps();

            // WHEN
            givenSut(SystemUnderTest.class, sut -> {
                sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            })
                    .when(SystemUnderTest::voidMethod)
                    .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
        }
    }
}
