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
import org.mockito.Mockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.TestAsYouThink.when;

public class ThenNoFailureTest {

    private GivenWhenThenDefinition gwtMock;

    @Before
    public void prepareFixtures() {
        gwtMock = Mockito.mock(GivenWhenThenDefinition.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_verify_no_failure_happened_during_the_execution_stage() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .when(sut -> { gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer(); })
                .thenItSucceeds();
    }

    @Test
    public void should_verify_no_failure_happened_during_the_execution_stage_while_starting_with_the_event() {
        // WHEN
        when(() -> gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer()).thenItSucceeds();
    }
}
