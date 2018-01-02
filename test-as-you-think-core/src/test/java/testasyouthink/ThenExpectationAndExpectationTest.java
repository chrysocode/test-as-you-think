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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;

public class ThenExpectationAndExpectationTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_verify_result_expectations_separately_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
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
    public void should_verify_expectations_separately_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    sut.voidMethod();
                })
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_verify_sut_expectations_separately_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    sut.voidMethod();
                })
                .then(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and(sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }
}
