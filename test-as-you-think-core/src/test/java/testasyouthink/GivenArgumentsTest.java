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

import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testasyouthink.GivenArgumentsTest.Parameter.Immutable;
import testasyouthink.GivenArgumentsTest.Parameter.ImmutableButUninstantiable;
import testasyouthink.GivenArgumentsTest.Parameter.ImmutableWithoutCopyConstructor;
import testasyouthink.GivenArgumentsTest.Parameter.Mutable;
import testasyouthink.GivenArgumentsTest.Parameter.MutableButUninstantiable;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.ParameterizedSystemUnderTest;
import testasyouthink.fixture.SystemUnderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static testasyouthink.TestAsYouThink.givenSut;

public class GivenArgumentsTest {

    private static final String GIVEN_STRING = "given argument";
    private static final int GIVEN_INTEGER = 201705;
    private static final boolean GIVEN_BOOLEAN = false;
    private static final String EXPECTED_RESULT = "expected result";
    private IMocksControl mocksControl;
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        mocksControl = createStrictControl();
        systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_one_argument_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithParameter(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_with_its_type_known_as_immutable() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithParameter(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(String.class, oneArgument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_with_its_immutable_type() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        class SystemUnderTestWithImmutableParameter extends ParameterizedSystemUnderTest<Immutable, Void, Void> {}
        SystemUnderTestWithImmutableParameter sutWithImmutableParameterMock = mocksControl.createMock(
                SystemUnderTestWithImmutableParameter.class);
        sutWithImmutableParameterMock.voidMethodWithParameter(anyObject(Immutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithImmutableParameterMock)
                .givenArgument(Immutable.class, immutableArgument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return new Immutable();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_with_its_mutable_type() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        class SystemUnderTestWithMutableParameter extends ParameterizedSystemUnderTest<Mutable, Void, Void> {}
        SystemUnderTestWithMutableParameter sutWithMutableParameterMock = mocksControl.createMock(
                SystemUnderTestWithMutableParameter.class);
        sutWithMutableParameterMock.voidMethodWithParameter(anyObject(Mutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithMutableParameterMock)
                .givenArgument(Mutable.class, mutableArgument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    mutableArgument.setForDemonstration(123);
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_fail_to_instantiate_one_argument_with_its_immutable_type_given_no_copy_constructor() throws
            Throwable {
        //GIVEN
        class SystemUnderTestWithUninstantiableParameter extends
                ParameterizedSystemUnderTest<ImmutableWithoutCopyConstructor, Void, Void> {}
        SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mocksControl.createMock(
                SystemUnderTestWithUninstantiableParameter.class);
        mocksControl.replay();

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                .givenArgument(ImmutableWithoutCopyConstructor.class, immutableButUninstantiable -> {
                    return new ImmutableWithoutCopyConstructor();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> {}));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Impossible to instantiate the argument of the " //
                        + "testasyouthink.GivenArgumentsTest$Parameter$ImmutableWithoutCopyConstructor" //
                        + " type! A copy constructor is missing.")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_fail_to_instantiate_one_argument_with_its_immutable_type() throws Throwable {
        //GIVEN
        class SystemUnderTestWithUninstantiableParameter extends
                ParameterizedSystemUnderTest<ImmutableButUninstantiable, Void, Void> {}
        SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mocksControl.createMock(
                SystemUnderTestWithUninstantiableParameter.class);
        mocksControl.replay();

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                .givenArgument(ImmutableButUninstantiable.class, immutableButUninstantiable -> {
                    return new ImmutableButUninstantiable(immutableButUninstantiable);
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> {}));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Impossible to instantiate the argument of the " //
                        + "testasyouthink.GivenArgumentsTest$Parameter$ImmutableButUninstantiable type!")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_fail_to_instantiate_one_argument_with_its_mutable_type() throws Throwable {
        //GIVEN
        class SystemUnderTestWithUninstantiableParameter extends
                ParameterizedSystemUnderTest<MutableButUninstantiable, Void, Void> {}
        SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mocksControl.createMock(
                SystemUnderTestWithUninstantiableParameter.class);
        mocksControl.replay();

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                .givenArgument(MutableButUninstantiable.class, mutableButUninstantiable -> {})
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> {}));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Impossible to instantiate the argument of the " //
                        + "testasyouthink.GivenArgumentsTest$Parameter$MutableButUninstantiable type!")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_receive_one_argument_with_its_description() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithParameter(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidMethodWithParameter(GIVEN_STRING)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .and("specified fixture",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::nonVoidMethodWithParameter)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_two_arguments_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.voidMethodWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .when(SystemUnderTest::voidMethodWithTwoParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_two_arguments_with_their_descriptions() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.voidMethodWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument 1 description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument("Argument 2 description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .when(SystemUnderTest::voidMethodWithTwoParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_two_arguments_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        expect(systemUnderTestMock.nonVoidMethodWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER)).andReturn(
                EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .when(SystemUnderTest::nonVoidMethodWithTwoParameters)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_two_arguments_with_their_types_given_a_second_mutable_argument() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        class SystemUnderTestWithTwoParameters extends ParameterizedSystemUnderTest<Mutable, Mutable, Void> {}
        SystemUnderTestWithTwoParameters sutWithTwoParameters = mocksControl.createMock(
                SystemUnderTestWithTwoParameters.class);
        sutWithTwoParameters.voidMethodWithTwoParameters(anyObject(Mutable.class), anyObject(Mutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithTwoParameters)
                .givenArgument(Mutable.class, argument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    argument.setForDemonstration(123);
                })
                .andArgument(Mutable.class, argument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    argument.setForDemonstration(456);
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithTwoParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_two_arguments_with_their_types_given_a_second_immutable_argument() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        class SystemUnderTestWithTwoParameters extends ParameterizedSystemUnderTest<Mutable, Immutable, Void> {}
        SystemUnderTestWithTwoParameters sutWithTwoParameters = mocksControl.createMock(
                SystemUnderTestWithTwoParameters.class);
        sutWithTwoParameters.voidMethodWithTwoParameters(anyObject(Mutable.class), anyObject(Immutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithTwoParameters)
                .givenArgument(Mutable.class, argument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    argument.setForDemonstration(123);
                })
                .andArgument(Immutable.class, argument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return new Immutable();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithTwoParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_three_arguments_given_a_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.voidMethodWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .when(SystemUnderTest::voidMethodWithThreeParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_three_arguments_with_their_descriptions() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.voidMethodWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument 1 description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument("Argument 2 description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument("Argument 3 description", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .when(SystemUnderTest::voidMethodWithThreeParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_three_arguments_given_a_non_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidMethodWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER,
                GIVEN_BOOLEAN)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .when(SystemUnderTest::nonVoidMethodWithThreeParameters)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_three_arguments_with_their_types_given_a_third_immutable_argument() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        class SystemUnderTestWithThreeParameters extends ParameterizedSystemUnderTest<Mutable, Mutable, Immutable> {}
        SystemUnderTestWithThreeParameters sutWithThreeParametersMock = mocksControl.createMock(
                SystemUnderTestWithThreeParameters.class);
        sutWithThreeParametersMock.voidMethodWithThreeParameters(anyObject(Mutable.class), anyObject(Mutable.class),
                anyObject(Immutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithThreeParametersMock)
                .givenArgument(Mutable.class, mutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .andArgument(Mutable.class, mutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .andArgument(Immutable.class, immutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return new Immutable();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithThreeParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_three_arguments_with_their_types_given_a_third_mutable_argument() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        class SystemUnderTestWithThreeParameters extends ParameterizedSystemUnderTest<Mutable, Mutable, Mutable> {}
        SystemUnderTestWithThreeParameters sutWithThreeParametersMock = mocksControl.createMock(
                SystemUnderTestWithThreeParameters.class);
        sutWithThreeParametersMock.voidMethodWithThreeParameters(anyObject(Mutable.class), anyObject(Mutable.class),
                anyObject(Mutable.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithThreeParametersMock)
                .givenArgument(Mutable.class, mutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .andArgument(Mutable.class, mutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .andArgument(Mutable.class, mutable -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithThreeParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_value_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithParameter(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument description", GIVEN_STRING)
                .when(SystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_value_given_a_void_method_and_two_preparation_steps() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithParameter(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenArgument("Argument description", GIVEN_STRING)
                .when(SystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_value_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithParameter(GIVEN_STRING)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument description", GIVEN_STRING)
                .when(SystemUnderTest::nonVoidMethodWithParameter)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_two_argument_values_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument 1 description", GIVEN_STRING)
                .andArgument("Argument 2 description", GIVEN_INTEGER)
                .when(SystemUnderTest::voidMethodWithTwoParameters)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_three_argument_values_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER,
                GIVEN_BOOLEAN)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("Argument 1 description", GIVEN_STRING)
                .andArgument("Argument 2 description", GIVEN_INTEGER)
                .andArgument("Argument 3 description", GIVEN_BOOLEAN)
                .when(SystemUnderTest::nonVoidMethodWithThreeParameters)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    public static class Parameter {

        @org.hibernate.annotations.Immutable
        public static class Immutable {

            public Immutable() {}

            public Immutable(Immutable value) {}
        }

        public static class Mutable {

            private int forDemonstration;

            public void setForDemonstration(int forDemonstration) {
                this.forDemonstration = forDemonstration;
            }
        }

        @org.hibernate.annotations.Immutable
        public static class ImmutableWithoutCopyConstructor {
            // Missing constructor able to receive another preexisting instance
        }

        @org.hibernate.annotations.Immutable
        public static class ImmutableButUninstantiable {

            public ImmutableButUninstantiable(ImmutableButUninstantiable value) throws InstantiationException {
                throw new InstantiationException("Impossible to instantiate it!");
            }
        }

        public static class MutableButUninstantiable {

            public MutableButUninstantiable() throws InstantiationException {
                throw new InstantiationException("Impossible to instantiate it!");
            }
        }
    }
}
