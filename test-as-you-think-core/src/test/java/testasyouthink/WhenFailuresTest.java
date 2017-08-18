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

import org.junit.After;
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
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.fixture.Specifications.ExpectedMessage.EXPECTED_EXECUTION_FAILURE_MESSAGE;

public class WhenFailuresTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhenFailuresTest.class);
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        systemUnderTestMock = strictMock(SystemUnderTest.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(systemUnderTestMock);
    }

    @Test
    public void should_fail_to_execute_given_a_non_void_method() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithThrowsClause()).andThrow(new UnexpectedException());
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
        replay(systemUnderTestMock, givenWhenThenDefinitionMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::nonVoidMethodWithThrowsClause)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                .hasCauseInstanceOf(UnexpectedException.class);
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_execute_given_a_void_method() throws Throwable {
        // GIVEN
        systemUnderTestMock.voidMethodWithThrowsClause();
        expectLastCall().andThrow(new UnexpectedException());
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
        replay(systemUnderTestMock, givenWhenThenDefinitionMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::voidMethodWithThrowsClause)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE)
                .hasCauseInstanceOf(UnexpectedException.class);
        verify(givenWhenThenDefinitionMock);
    }
}
