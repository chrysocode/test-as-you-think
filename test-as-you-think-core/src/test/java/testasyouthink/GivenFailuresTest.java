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

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.GivenFailuresTest.Parameter.MutableButUninstantiable;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.ParameterizedSystemUnderTest;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.preparation.PreparationError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class GivenFailuresTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenFailuresTest.class);
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);
    }

    @Test
    public void should_fail_to_create_a_sut_instance() throws Throwable {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTestFailingToBeInstantiated.class)
                .when(SystemUnderTestFailingToBeInstantiated::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to instantiate the system under test!")
                .hasCauseInstanceOf(NullPointerException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_supply_a_sut_instance() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(() -> new SystemUnderTestFailingToBeInstantiated())
                .when(SystemUnderTestFailingToBeInstantiated::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(NullPointerException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_the_sut_after_instantiating_it() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(SystemUnderTest.class, sut -> {
            throw new UnexpectedException();
        })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_the_sut() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_the_sut_with_its_specification() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given("SUT specification", sut -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_the_sut_with_its_specifications() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given("SUT specification that passes", sut -> {})
                .and("SUT specification that fails", sut -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the system under test!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_an_ordinary_fixture() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the test fixture!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_an_ordinary_fixture_with_its_specification() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given("fixture specification that fails", () -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the test fixture!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_prepare_a_second_ordinary_fixture_with_its_specification() {
        // WHEN
        Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                .given("fixture specification that passes", () -> {
                })
                .and("fixture specification that fails", () -> {
                    throw new UnexpectedException();
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare the test fixture!")
                .hasCauseInstanceOf(UnexpectedException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_fail_to_instantiate_one_argument_with_its_mutable_type() throws Throwable {
        //GIVEN
        class SystemUnderTestWithUninstantiableParameter extends
                ParameterizedSystemUnderTest<MutableButUninstantiable, Void, Void> {}
        SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mock(
                SystemUnderTestWithUninstantiableParameter.class);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                .givenArgument(MutableButUninstantiable.class, mutableButUninstantiable -> {})
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to instantiate the argument of the " //
                        + "testasyouthink.GivenFailuresTest$Parameter$MutableButUninstantiable type!")
                .hasCauseInstanceOf(InstantiationException.class);
        verifyZeroInteractions(givenWhenThenDefinitionMock);
    }

    public static class SystemUnderTestFailingToBeInstantiated {

        public SystemUnderTestFailingToBeInstantiated() throws Exception {
            throw new NullPointerException("Impossible to instantiate it!");
        }

        public void voidMethod() {}
    }

    public static class Parameter {

        public static class MutableButUninstantiable {

            public MutableButUninstantiable() throws InstantiationException {
                throw new InstantiationException("Impossible to instantiate it!");
            }
        }
    }
}