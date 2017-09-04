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

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.execution.ExecutionError;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.when;
import static testasyouthink.fixture.Specifications.ExpectedMessage.EXPECTED_EXECUTION_FAILURE_MESSAGE;

public class WhenFailuresTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhenFailuresTest.class);
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinition;

    @Before
    public void prepareFixtures() {
        // GIVEN
        systemUnderTestMock = mock(SystemUnderTest.class);
        givenWhenThenDefinition = mock(GivenWhenThenDefinition.class);
    }

    @Test
    public void should_fail_to_execute_given_a_non_void_method() throws Throwable {
        // GIVEN
        when(systemUnderTestMock.nonVoidMethodWithThrowsClause()).thenThrow(UnexpectedException.class);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::nonVoidMethodWithThrowsClause)
                .then(() -> givenWhenThenDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinition);
    }

    @Test
    public void should_fail_to_execute_given_a_void_method() throws Throwable {
        // GIVEN
        doThrow(UnexpectedException.class)
                .when(systemUnderTestMock)
                .voidMethodWithThrowsClause();

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::voidMethodWithThrowsClause)
                .then(() -> givenWhenThenDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinition);
    }

    @Test
    public void should_fail_to_execute_directly_a_non_void_method() {
        // WHEN
        Throwable thrown = catchThrowable(() -> when(() -> {
            throw new UnexpectedException();
        }).then(() -> givenWhenThenDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinition);
    }
}
