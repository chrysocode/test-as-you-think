package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

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
}
