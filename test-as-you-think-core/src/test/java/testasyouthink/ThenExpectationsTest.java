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
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

class ThenExpectationsTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @AfterEach
    void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Nested
    class When_returning_a_result {

        @Test
        void should_verify_result_expectations_separately() {
            // GIVEN
            givenWhenThenDefinitionMock = orderedSteps(0, 3);

            // WHEN
            givenSutClass(SystemUnderTest.class)
                    .when(sut -> {
                        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        return sut.nonVoidMethod();
                    })
                    .then(result -> {
                        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        assertThat(result).startsWith("expected");
                    })
                    .and(result -> {
                        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        assertThat(result).contains(" ");
                    })
                    .and(result -> {
                        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                        assertThat(result).endsWith("result");
                    });
        }

        @Test
        void should_verify_expectations_on_both_the_result_and_the_sut() {
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
    class When_returning_nothing {

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
        void should_verify_sut_expectations_separately() {
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
    }
}
