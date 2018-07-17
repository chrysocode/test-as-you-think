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
import org.junit.jupiter.api.BeforeEach;
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
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

class GivenOrdinaryFixturesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenOrdinaryFixturesTest.class);
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Nested
    class Given_specified_fixtures {

        private static final String EXPECTED_RESULT = "expected result";

        @AfterEach
        void verifyMocks() {
            // THEN
            verify(givenWhenThenDefinitionMock);
        }

        @Test
        void should_specify_a_fixture() {
            // GIVEN
            givenWhenThenDefinitionMock = orderedSteps(1);

            // WHEN
            givenSutClass(SystemUnderTest.class)
                    .given("what it makes this fixture specific to the current use case",
                            () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                    .when(sut -> {
                        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        return sut.nonVoidMethod();
                    })
                    .then(result -> {
                        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        assertThat(result).isEqualTo(EXPECTED_RESULT);
                    });
        }

        @Test
        void should_specify_multiple_ordinary_fixtures() {
            // GIVEN
            givenWhenThenDefinitionMock = orderedSteps(3);

            // WHEN
            givenSutClass(SystemUnderTest.class)
                    .given("what it makes the first fixture specific to the current use case",
                            () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                    .and("what it makes the second fixture specific to the current use case",
                            () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                    .and("what it makes the third fixture specific to the current use case",
                            () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                    .when(sut -> {
                        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        return sut.nonVoidMethod();
                    })
                    .then(result -> {
                        assertThat(result).isEqualTo(EXPECTED_RESULT);
                        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    });
        }
    }

    @Nested
    class Failing_to_prepare_ordinary_fixtures {

        @BeforeEach
        void prepareFixtures() {
            // GIVEN
            givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);
        }

        private void assertThatItFailsToPrepareTestFixture(Throwable thrown) {
            LOGGER.debug("Stack trace", thrown);
            assertThat(thrown)
                    .isInstanceOf(PreparationError.class)
                    .hasMessage("Fails to prepare the test fixture!")
                    .hasCauseInstanceOf(UnexpectedException.class);
            verifyZeroInteractions(givenWhenThenDefinitionMock);
        }

        @Test
        void should_fail_to_prepare_an_ordinary_fixture() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .given(() -> {
                        throw new UnexpectedException();
                    })
                    .when(SystemUnderTest::voidMethod)
                    .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

            // THEN
            assertThatItFailsToPrepareTestFixture(thrown);
        }

        @Test
        void should_fail_to_prepare_an_ordinary_fixture_with_its_specification() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .given("fixture specification that fails", () -> {
                        throw new UnexpectedException();
                    })
                    .when(SystemUnderTest::voidMethod)
                    .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

            // THEN
            assertThatItFailsToPrepareTestFixture(thrown);
        }

        @Test
        void should_fail_to_prepare_a_second_ordinary_fixture_with_its_specification() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .given("fixture specification that passes", () -> {
                    })
                    .and("fixture specification that fails", () -> {
                        throw new UnexpectedException();
                    })
                    .when(SystemUnderTest::voidMethod)
                    .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

            // THEN
            assertThatItFailsToPrepareTestFixture(thrown);
        }
    }
}
