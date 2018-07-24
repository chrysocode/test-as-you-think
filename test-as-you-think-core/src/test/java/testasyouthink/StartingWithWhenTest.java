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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.execution.ExecutionError;
import testasyouthink.fixture.ExpectedException;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedRunnable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static testasyouthink.TestAsYouThink.when;
import static testasyouthink.TestAsYouThink.whenOutsideOperatingConditions;
import static testasyouthink.fixture.Specifications.ExpectedMessage.EXPECTED_EXECUTION_FAILURE_MESSAGE;

/**
 * Acceptance testing to play a shortened When-Then scenario without a preparation step.
 */
class StartingWithWhenTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartingWithWhenTest.class);

    @Nested
    class When_returning_nothing {

        @Test
        void should_directly_execute_a_void_target_method() {
            // GIVEN
            SystemUnderTest sut = mock(SystemUnderTest.class);
            GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

            // WHEN
            when(sut::voidMethod).then(() -> gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

            // THEN
            InOrder inOrder = Mockito.inOrder(sut, gwtMock);
            inOrder
                    .verify(sut)
                    .voidMethod();
            inOrder
                    .verify(gwtMock)
                    .thenTheActualResultIsInKeepingWithTheExpectedResult();
            inOrder.verifyNoMoreInteractions();
        }

        @Nested
        class When_an_unexpected_failure_happens {

            @Test
            void should_fail_to_directly_execute_a_void_target_method() {
                // GIVEN
                GivenWhenThenDefinition givenWhenThenDefinition = mock(GivenWhenThenDefinition.class);

                // WHEN
                Throwable thrown = catchThrowable(() -> when((CheckedRunnable) () -> {
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
    }

    @Nested
    class When_returning_a_result {

        @Test
        void should_directly_execute_a_non_void_target_method() {
            // GIVEN
            SystemUnderTest sut = mock(SystemUnderTest.class);
            when(sut.nonVoidMethod()).thenReturn("expected result");
            GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

            // WHEN
            when(sut::nonVoidMethod).then(result -> {
                assertThat(result).isEqualTo("expected result");
                gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            });

            // THEN
            InOrder inOrder = Mockito.inOrder(sut, gwtMock);
            inOrder
                    .verify(sut)
                    .nonVoidMethod();
            inOrder
                    .verify(gwtMock)
                    .thenTheActualResultIsInKeepingWithTheExpectedResult();
            inOrder.verifyNoMoreInteractions();
        }

        @Nested
        class When_an_unexpected_failure_happens {

            @Test
            void should_fail_to_directly_execute_a_non_void_target_method() {
                // GIVEN
                GivenWhenThenDefinition givenWhenThenDefinition = mock(GivenWhenThenDefinition.class);

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
    }

    @Nested
    class Then_a_failure_is_expected {

        @Test
        void should_verify_an_expected_failure() throws Throwable {
            //GIVEN
            SystemUnderTest systemUnderTestMock = mock(SystemUnderTest.class);
            when(systemUnderTestMock.nonVoidMethodWithThrowsClause()).thenThrow(ExpectedException.class);

            // WHEN
            whenOutsideOperatingConditions(systemUnderTestMock::nonVoidMethodWithThrowsClause).thenItFails();

            // THEN
            verify(systemUnderTestMock).nonVoidMethodWithThrowsClause();
            verifyNoMoreInteractions(systemUnderTestMock);
        }

        @Test
        void should_fail_to_verify_an_expected_failure() throws Throwable {
            //GIVEN
            SystemUnderTest systemUnderTestMock = mock(SystemUnderTest.class);
            when(systemUnderTestMock.nonVoidMethodWithThrowsClause()).thenReturn("expected result");

            // WHEN
            Throwable thrown = catchThrowable(() -> whenOutsideOperatingConditions(
                    systemUnderTestMock::nonVoidMethodWithThrowsClause).thenItFails());

            // THEN
            LOGGER.debug("Stack trace", thrown);
            assertThat(thrown).isInstanceOf(AssertionError.class);
            verify(systemUnderTestMock).nonVoidMethodWithThrowsClause();
            verifyNoMoreInteractions(systemUnderTestMock);
        }
    }
}
