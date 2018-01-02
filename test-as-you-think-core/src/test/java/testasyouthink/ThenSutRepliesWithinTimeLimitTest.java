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

import org.junit.Before;
import org.junit.Test;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.function.CheckedConsumer;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenSutRepliesWithinTimeLimitTest {

    private GivenWhenThenDefinition gwtDefinition;

    @Before
    public void prepareFixtures() {
        gwtDefinition = mock(GivenWhenThenDefinition.class);
    }

    @Test
    public void should_reply_within_a_time_limit_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    gwtDefinition.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sleep(50);
                })
                .whenSutRuns(sut -> gwtDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer())
                .thenSutRepliesWithin(49)
                .and(() -> gwtDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(gwtDefinition).givenAContextThatDefinesTheInitialStateOfTheSystem();
        verify(gwtDefinition).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verify(gwtDefinition).thenTheActualResultIsInKeepingWithTheExpectedResult();
        verifyNoMoreInteractions(gwtDefinition);
    }

    @Test
    public void should_reply_within_a_time_limit_given_a_method_with_a_parameter() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenArgument(() -> {
                    gwtDefinition.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sleep(50);
                    return "argument";
                })
                .when((sut, argument) -> {
                    gwtDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                })
                .thenSutRepliesWithin(49)
                .and(() -> gwtDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(gwtDefinition).givenAContextThatDefinesTheInitialStateOfTheSystem();
        verify(gwtDefinition).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verify(gwtDefinition).thenTheActualResultIsInKeepingWithTheExpectedResult();
        verifyNoMoreInteractions(gwtDefinition);
    }

    @Test
    public void should_reply_within_a_time_limit_given_a_method_with_a_mutable_parameter() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenArgument(StringBuilder.class, mutable -> {
                    gwtDefinition.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    mutable.append("argument");
                    sleep(50);
                })
                .when((sut, argument) -> {
                    gwtDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                })
                .thenSutRepliesWithin(49)
                .and(() -> gwtDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(gwtDefinition).givenAContextThatDefinesTheInitialStateOfTheSystem();
        verify(gwtDefinition).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verify(gwtDefinition).thenTheActualResultIsInKeepingWithTheExpectedResult();
        verifyNoMoreInteractions(gwtDefinition);
    }

    @Test
    public void should_reply_within_a_time_limit_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    gwtDefinition.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sleep(50);
                })
                .when(sut -> {
                    gwtDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return "expected result";
                })
                .thenSutRepliesWithin(49)
                .and(result -> {
                    assertThat(result).isEqualTo("expected result");
                    gwtDefinition.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verify(gwtDefinition).givenAContextThatDefinesTheInitialStateOfTheSystem();
        verify(gwtDefinition).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verify(gwtDefinition).thenTheActualResultIsInKeepingWithTheExpectedResult();
        verifyNoMoreInteractions(gwtDefinition);
    }

    @Test
    public void should_fail_to_reply_within_a_time_limit_given_a_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    sleep(1000);
                })
                .thenSutRepliesWithin(1))
                .isInstanceOf(AssertionError.class)
                .hasMessage("test timed out after 1 milliseconds")
                .hasCauseInstanceOf(TimeoutException.class);
    }

    @Test
    public void should_fail_to_reply_within_a_duration_limit_given_a_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    sleep(1000);
                })
                .thenSutRepliesWithin(Duration.ofMillis(1)))
                .isInstanceOf(AssertionError.class)
                .hasMessage("test timed out after 1 milliseconds")
                .hasCauseInstanceOf(TimeoutException.class);
    }

    @Test
    public void should_fail_to_execute_when_the_current_thread_was_interrupted_while_waiting_given_a_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    currentThread()
                            .getThreadGroup()
                            .getParent()
                            .interrupt();
                })
                .thenSutRepliesWithin(1000))
                .isInstanceOf(AssertionError.class)
                .hasMessage("the current thread was interrupted while waiting")
                .hasCauseInstanceOf(InterruptedException.class);
    }

    @Test
    public void should_fail_to_execute_when_the_computation_throws_an_exception_given_a_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when((CheckedConsumer<SystemUnderTest>) sut -> {
                    throw new Exception("unexpected exception");
                })
                .thenSutRepliesWithin(1000))
                .isInstanceOf(AssertionError.class)
                .hasMessage("the computation threw an exception")
                .hasCauseInstanceOf(ExecutionException.class);
    }

    @Test
    public void should_fail_to_reply_within_a_time_limit_given_a_non_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    sleep(1000);
                    return null;
                })
                .thenSutRepliesWithin(1))
                .isInstanceOf(AssertionError.class)
                .hasMessage("test timed out after 1 milliseconds")
                .hasCauseInstanceOf(TimeoutException.class);
    }

    @Test
    public void should_fail_to_reply_within_a_duration_limit_given_a_non_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    sleep(1000);
                    return null;
                })
                .thenSutRepliesWithin(Duration.ofMillis(1)))
                .isInstanceOf(AssertionError.class)
                .hasMessage("test timed out after 1 milliseconds")
                .hasCauseInstanceOf(TimeoutException.class);
    }
}
