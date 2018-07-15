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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedRunnable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.TestAsYouThink.when;

class ThenNoFailureTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThenNoFailureTest.class);
    private GivenWhenThenDefinition gwtMock;

    @BeforeEach
    void prepareFixtures() {
        gwtMock = Mockito.mock(GivenWhenThenDefinition.class);
    }

    @AfterEach
    void verifyMocks() {
        // THEN
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    void should_verify_no_failure_happened_during_the_execution_stage() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .when(sut -> { gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer(); })
                .thenItSucceeds();
    }

    @Test
    void should_verify_no_failure_happened_during_the_execution_stage_while_starting_with_the_event() {
        // WHEN
        when(() -> gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer()).thenItSucceeds();
    }

    @Test
    void should_fail_to_verify_no_failure_happened_during_the_execution_stage() {
        // WHEN
        Throwable thrown = catchThrowable(() -> when((CheckedRunnable) () -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            throw new UnexpectedException();
        }).thenItSucceeds());

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining(UnexpectedException.class.getName());
    }
}
