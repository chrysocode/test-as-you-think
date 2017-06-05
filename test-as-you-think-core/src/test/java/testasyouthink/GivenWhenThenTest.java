/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 Xavier Pigeon and TestAsYouThink contributors
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

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

public class GivenWhenThenTest {

    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_non_void_method() {
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
    public void should_follow_the_given_when_then_full_sequence_given_a_void_method() {
        // WHEN
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_sut_class_to_be_instantiated() {
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
    public void should_verify_expectations_on_the_sut_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then((sut, result) -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    assertThat(sut.getState()).isNotNull();
                });
    }

    @Test
    public void should_verify_expectations_on_the_sut_given_a_void_method() {
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
}
