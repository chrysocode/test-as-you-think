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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.StringAssert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenResult;
import testasyouthink.fixture.GivenWhenThenDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.when;

public class ThenFluentAssertionsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThenFluentAssertionsTest.class);
    private GivenWhenThenDefinition gwtMock;

    @Before
    public void prepareFixtures() {
        gwtMock = mock(GivenWhenThenDefinition.class);
    }

    @Test
    public void should_verify_the_actual_string_is_equal_to_an_expected_result() {
        // WHEN
        when(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "expected result";
        })
                .thenResult()
                .isEqualTo("expected result");

        // THEN
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_fail_to_verify_the_actual_string_is_not_equal_to_an_expected_result() {
        // WHEN
        Throwable thrown = catchThrowable(() -> when(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "actual result";
        })
                .thenResult()
                .isEqualTo("expected result"));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown).isInstanceOf(AssertionError.class);
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_compile_before_running() {
        ThenResultStep<StringAssert> thenResultStep = new ThenResultStep<>(new StringAssert("plein de bonnes choses"));
        thenResultStep
                .thenResult()
                .contains("bonnes");
        ThenResult<? extends AbstractAssert> thenResult = when(() -> "expected result");
        StringAssert stringAssert = thenResult.thenResult();
        stringAssert.contains("expected result");
    }

    @Test
    public void should_verify_the_actual_string_contains_an_expected_word() {
        // WHEN
        when(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "expected result";
        })
                .thenResult()
                .isEqualTo("expected result");
        //.contains("expected");

        // THEN
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_fail_to_verify_the_actual_string_contains_an_expected_word() {
        // WHEN
        Throwable thrown = catchThrowable(() -> when(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "actual";
        })
                .thenResult()
                .isEqualTo("expected"));
        //.contains("expected"));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown).isInstanceOf(AssertionError.class);
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }
}
