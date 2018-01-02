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

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenStdoutAsResultTest {

    @Test
    public void should_verify_the_standard_output_as_a_result_given_a_void_target_method() {
        // GIVEN
        GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStdoutToBeCaptured()
                .when(sut -> {
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    System.out.println("Stdout as a result");
                })
                .thenStdoutAsResult(result -> {
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).hasContent("Stdout as a result");
                });

        // THEN
        InOrder inOrder = Mockito.inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock)
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }
}
