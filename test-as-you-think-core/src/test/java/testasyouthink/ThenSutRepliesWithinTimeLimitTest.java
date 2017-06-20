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

import org.junit.Test;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.function.CheckedConsumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenSutRepliesWithinTimeLimitTest {

    @Test
    public void should_fail_given_a_too_slow_void_method() {
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
    public void should_fail_when_the_current_thread_was_interrupted_while_waiting_given_a_void_method() {
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
    public void should_fail_when_the_computation_throws_an_exception_given_a_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when((CheckedConsumer<SystemUnderTest>) sut -> {
                    throw new Exception("unexpected exception");
                })
                .thenSutRepliesWithin(1000))
                .isInstanceOf(AssertionError.class)
                .hasMessage("the computation threw an exception")
                .hasCauseInstanceOf(ExecutionException.class);
    }
}
