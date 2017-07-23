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
import org.mockito.InOrder;
import org.mockito.Mockito;
import testasyouthink.fixture.ExpectedException;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static testasyouthink.TestAsYouThink.when;
import static testasyouthink.TestAsYouThink.whenOutsideOperatingConditions;

public class StartingWithWhenTest {

    @Test
    public void should_start_with_when_given_a_void_method() {
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

    @Test
    public void should_start_with_when_given_a_non_void_method() {
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

    @Test
    public void should_verify_an_expected_failure_by_starting_by_the_when_step() throws Throwable {
        //GIVEN
        SystemUnderTest systemUnderTestMock = mock(SystemUnderTest.class);
        when(systemUnderTestMock.nonVoidMethodWithThrowsClause()).thenThrow(ExpectedException.class);

        // WHEN
        whenOutsideOperatingConditions(systemUnderTestMock::nonVoidMethodWithThrowsClause).thenItFails();

        // THEN
        verify(systemUnderTestMock).nonVoidMethodWithThrowsClause();
        verifyNoMoreInteractions(systemUnderTestMock);
    }
}
