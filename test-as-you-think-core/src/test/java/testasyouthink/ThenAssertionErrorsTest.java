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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenAssertionErrorsTest {

    @Test
    public void should_get_an_assertion_error_from_a_runnable_step_given_a_void_target_method() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {})
                .then(() -> assertThat(true).isFalse()));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasNoCause();
    }

    @Test
    public void should_get_an_assertion_error_from_a_specified_runnable_step_given_a_void_target_method() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {})
                .then("Expectations", () -> assertThat(true).isFalse()));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasNoCause();
    }
}
