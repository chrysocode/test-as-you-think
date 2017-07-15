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
import org.hibernate.annotations.Immutable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        class SystemUnderTestWithImmutableParameter extends ParameterizedSystemUnderTest<ImmutableParameter, Void,
                Void> {}
        SystemUnderTestWithImmutableParameter sutWithImmutableParameterMock = mocksControl.createMock(
                SystemUnderTestWithImmutableParameter.class);
        sutWithImmutableParameterMock.voidMethodWithParameter(anyObject(ImmutableParameter.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithImmutableParameterMock)
                .givenArgument(ImmutableParameter.class, immutableArgument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return new ImmutableParameter();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_receive_one_argument_with_its_mutable_type() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        class SystemUnderTestWithMutableParameter extends ParameterizedSystemUnderTest<MutableParameter, Void, Void> {}
        SystemUnderTestWithMutableParameter sutWithMutableParameterMock = mocksControl.createMock(
                SystemUnderTestWithMutableParameter.class);
        sutWithMutableParameterMock.voidMethodWithParameter(anyObject(MutableParameter.class));
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(sutWithMutableParameterMock)
                .givenArgument(MutableParameter.class, mutableArgument -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    mutableArgument.setMutableForDemonstration(123);
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_fail_to_instantiate_one_argument_with_its_immutable_type() throws Throwable {
        //GIVEN
        class SystemUnderTestWithUninstantiableParameter extends
                ParameterizedSystemUnderTest<UninstantiableImmutableParameter, Void, Void> {}
        SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mocksControl.createMock(
                SystemUnderTestWithUninstantiableParameter.class);
        mocksControl.replay();

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                .givenArgument(UninstantiableImmutableParameter.class, immutableButUninstantiable -> {
                    return new UninstantiableImmutableParameter();
                })
                .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                .then(() -> {}));

        // THEN
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Impossible to instantiate the argument of the " //
                        + "testasyouthink.GivenArgumentsTest$UninstantiableImmutableParameter" //
                        + " type! A copy constructor is missing.")
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

    @Immutable
    public static class UninstantiableImmutableParameter {
        // Missing constructor able to receive another preexisting instance
    }

    @Immutable
    public static class ImmutableParameter {

        public ImmutableParameter() {}

        public ImmutableParameter(ImmutableParameter value) {}
    }

    public static class MutableParameter {

        private int mutableForDemonstration;

        public void setMutableForDemonstration(int mutableForDemonstration) {
            this.mutableForDemonstration = mutableForDemonstration;
        }
    }
}
