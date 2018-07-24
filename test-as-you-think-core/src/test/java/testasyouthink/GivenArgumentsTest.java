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

import org.assertj.core.api.AbstractAssert;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.GivenArgumentsTest.Parameter.Mutable;
import testasyouthink.GivenArgumentsTest.Parameter.MutableButUninstantiable;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.ParameterizedSystemUnderTest;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.preparation.PreparationError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.mockito.Mockito.mock;
import static testasyouthink.ArgumentPreparationAssertions.assertThatFailure;
import static testasyouthink.GivenArgumentsTest.GivenData.EXPECTED_RESULT;
import static testasyouthink.GivenArgumentsTest.GivenData.GIVEN_BOOLEAN;
import static testasyouthink.GivenArgumentsTest.GivenData.GIVEN_INTEGER;
import static testasyouthink.GivenArgumentsTest.GivenData.GIVEN_STRING;
import static testasyouthink.TestAsYouThink.givenSut;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenArgumentsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenArgumentsTest.class);
    private IMocksControl mocksControl;
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @BeforeEach
    void prepareFixtures() {
        mocksControl = createStrictControl();
        systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
    }

    @AfterEach
    void verifyMocks() {
        // THEN
        mocksControl.verify();
    }

    static class GivenData {

        static final String GIVEN_STRING = "given argument";
        static final int GIVEN_INTEGER = 201705;
        static final boolean GIVEN_BOOLEAN = false;
        static final String EXPECTED_RESULT = "expected result";
    }

    public static class Parameter {

        public static class Mutable {

            private int forDemonstration;

            void setForDemonstration(int forDemonstration) {
                this.forDemonstration = forDemonstration;
            }
        }

        public static class MutableButUninstantiable {

            public MutableButUninstantiable() throws InstantiationException {
                throw new InstantiationException("Impossible to instantiate it!");
            }
        }
    }

    @Nested
    class When_receiving_one_argument {

        @Nested
        class Given_an_argument_to_be_built {

            @Test
            void should_receive_one_argument_given_a_void_method() {
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
            void should_receive_one_argument_with_its_mutable_type() {
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
            void should_receive_one_argument_with_its_description() {
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
            void should_receive_one_argument_given_a_non_void_method() {
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

            @Nested
            class Failing_to_build_one_argument {

                @BeforeEach
                void prepareMocks() {
                    mocksControl.replay();
                }

                @Test
                void should_fail_to_supply_one_argument() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                            .givenArgument((CheckedSupplier<String>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithParameter)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_supply_one_argument_with_its_specification() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                            .givenArgument("argument specification", (CheckedSupplier<String>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithParameter)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_instantiate_one_argument_with_its_mutable_type() {
                    //GIVEN
                    class SystemUnderTestWithUninstantiableParameter extends
                            ParameterizedSystemUnderTest<MutableButUninstantiable, Void, Void> {}
                    SystemUnderTestWithUninstantiableParameter sutWithUninstantiableParameter = mock(
                            SystemUnderTestWithUninstantiableParameter.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(sutWithUninstantiableParameter)
                            .givenArgument(MutableButUninstantiable.class, mutableButUninstantiable -> {})
                            .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    LOGGER.debug("Stack trace", thrown);
                    assertThat(thrown)
                            .isInstanceOf(PreparationError.class)
                            .hasMessage("Fails to instantiate the argument of the " //
                                    + "testasyouthink.GivenArgumentsTest$Parameter$MutableButUninstantiable type!")
                            .hasCauseInstanceOf(InstantiationException.class);
                }

                @Test
                void should_fail_to_prepare_one_argument_with_its_mutable_type() {
                    //GIVEN
                    class SystemUnderTestWithMutableParameter extends ParameterizedSystemUnderTest<Parameter.Mutable,
                            Void, Void> {}
                    SystemUnderTestWithMutableParameter sutWithMutableParameter = mock(
                            SystemUnderTestWithMutableParameter.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(sutWithMutableParameter)
                            .givenArgument(Parameter.Mutable.class, mutable -> {
                                throw new UnexpectedException();
                            })
                            .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithParameter)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingMutableArgument();
                }
            }
        }

        @Nested
        class Given_an_argument_value {

            @Test
            void should_receive_one_argument_value_given_a_void_method() {
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
            void should_receive_one_argument_value_given_a_void_method_and_two_preparation_steps() {
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
            void should_receive_one_argument_value_given_a_non_void_method() {
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
        }
    }

    @Nested
    class When_receiving_two_arguments {

        @Nested
        class Given_arguments_to_be_built {

            @Test
            void should_receive_two_arguments_given_a_void_method() {
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
            void should_receive_two_arguments_with_their_descriptions() {
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
            void should_receive_two_arguments_given_a_non_void_method() {
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
            void should_receive_two_arguments_with_their_types_given_a_second_mutable_argument() {
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

            @Nested
            class Failing_to_build_a_second_argument {

                @BeforeEach
                void prepareMocks() {
                    mocksControl.replay();
                }

                @Test
                void should_fail_to_supply_a_second_argument() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .givenArgument(() -> "argument")
                            .andArgument((CheckedSupplier<Integer>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithTwoParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_supply_a_second_argument_with_its_specification() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .givenArgument(() -> "argument")
                            .andArgument("argument specification", (CheckedSupplier<Integer>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithTwoParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_prepare_a_second_argument_with_its_mutable_type() {
                    //GIVEN
                    class SystemUnderTestWithMutableParameter extends ParameterizedSystemUnderTest<Parameter.Mutable,
                            Parameter.Mutable, Void> {}
                    SystemUnderTestWithMutableParameter sutWithMutableParameters = mock(
                            SystemUnderTestWithMutableParameter.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(sutWithMutableParameters)
                            .givenArgument(Parameter.Mutable.class, mutable -> {})
                            .andArgument(Parameter.Mutable.class, mutable -> {
                                throw new UnexpectedException();
                            })
                            .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithTwoParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingMutableArgument();
                }
            }
        }

        @Nested
        class Given_argument_values {

            @Test
            void should_receive_two_argument_values_given_a_void_method() {
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
        }
    }

    @Nested
    class When_receiving_three_arguments {

        @Nested
        class Given_arguments_to_be_built {

            @Test
            void should_receive_three_arguments_given_a_void_method() {
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
            void should_receive_three_arguments_with_their_descriptions() {
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
            void should_receive_three_arguments_given_a_non_void_method() {
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
            void should_receive_three_arguments_with_their_types_given_a_third_mutable_argument() {
                //GIVEN
                givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                expectLastCall().times(3);
                class SystemUnderTestWithThreeParameters extends ParameterizedSystemUnderTest<Mutable, Mutable,
                        Mutable> {}
                SystemUnderTestWithThreeParameters sutWithThreeParametersMock = mocksControl.createMock(
                        SystemUnderTestWithThreeParameters.class);
                sutWithThreeParametersMock.voidMethodWithThreeParameters(anyObject(Mutable.class),
                        anyObject(Mutable.class), anyObject(Mutable.class));
                givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                mocksControl.replay();

                // WHEN
                givenSut(sutWithThreeParametersMock)
                        .givenArgument(Mutable.class,
                                mutable -> givenWhenThenDefinitionMock
                                        .givenAContextThatDefinesTheInitialStateOfTheSystem())
                        .andArgument(Mutable.class,
                                mutable -> givenWhenThenDefinitionMock
                                        .givenAContextThatDefinesTheInitialStateOfTheSystem())
                        .andArgument(Mutable.class,
                                mutable -> givenWhenThenDefinitionMock
                                        .givenAContextThatDefinesTheInitialStateOfTheSystem())
                        .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithThreeParameters)
                        .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
            }

            @Nested
            class Failing_to_build_a_third_argument {

                @BeforeEach
                void prepareMocks() {
                    mocksControl.replay();
                }

                @Test
                void should_fail_to_supply_a_third_argument() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .givenArgument(() -> "argument")
                            .andArgument(() -> 2)
                            .andArgument((CheckedSupplier<Boolean>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithThreeParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_supply_a_third_argument_with_its_specification() {
                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                            .givenArgument(() -> "argument")
                            .andArgument(() -> 2)
                            .andArgument("argument specification", (CheckedSupplier<Boolean>) () -> {
                                throw new UnexpectedException();
                            })
                            .when(SystemUnderTest::voidMethodWithThreeParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingArgument();
                }

                @Test
                void should_fail_to_prepare_a_third_argument_with_its_mutable_type() {
                    //GIVEN
                    class SystemUnderTestWithMutableParameter extends ParameterizedSystemUnderTest<Parameter.Mutable,
                            Parameter.Mutable, Parameter.Mutable> {}
                    SystemUnderTestWithMutableParameter sutWithMutableParameters = mock(
                            SystemUnderTestWithMutableParameter.class);

                    // WHEN
                    Throwable thrown = catchThrowable(() -> givenSut(sutWithMutableParameters)
                            .givenArgument(Parameter.Mutable.class, mutable -> {})
                            .andArgument(Parameter.Mutable.class, mutable -> {})
                            .andArgument(Parameter.Mutable.class, mutable -> {
                                throw new UnexpectedException();
                            })
                            .whenSutRuns(ParameterizedSystemUnderTest::voidMethodWithThreeParameters)
                            .then(() -> givenWhenThenDefinitionMock
                                    .thenTheActualResultIsInKeepingWithTheExpectedResult()));

                    // THEN
                    assertThatFailure(thrown).happensWhilePreparingMutableArgument();
                }
            }
        }

        @Nested
        class Given_argument_values {

            @Test
            void should_receive_three_argument_values_given_a_non_void_method() {
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
        }
    }
}

class ArgumentPreparationAssertions extends AbstractAssert<ArgumentPreparationAssertions, Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentPreparationAssertions.class);

    private ArgumentPreparationAssertions(Throwable throwable) {
        super(throwable, ArgumentPreparationAssertions.class);
    }

    static ArgumentPreparationAssertions assertThatFailure(Throwable thrown) {
        return new ArgumentPreparationAssertions(thrown);
    }

    void happensWhilePreparingArgument() {
        LOGGER.debug("Stack trace", actual);
        assertThat(actual)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare an argument for the target method!")
                .hasCauseInstanceOf(UnexpectedException.class);
    }

    void happensWhilePreparingMutableArgument() {
        LOGGER.debug("Stack trace", actual);
        assertThat(actual)
                .isInstanceOf(PreparationError.class)
                .hasMessage("Fails to prepare an argument of the " //
                        + "testasyouthink.GivenArgumentsTest$Parameter$Mutable type for the target method!")
                .hasCauseInstanceOf(UnexpectedException.class);
    }
}