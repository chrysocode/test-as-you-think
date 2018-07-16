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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenFailuresTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenFailuresTest.class);
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @BeforeEach
    void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);
    }

    @Nested
    class Given_ordinary_fixtures {

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
