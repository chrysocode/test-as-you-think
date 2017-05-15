package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
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

    @Test
    public void should_receive_an_argument_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithArgument(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidMethodWithArgument(GIVEN_STRING)).andReturn(EXPECTED_RESULT);
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
                .when(SystemUnderTest::nonVoidMethodWithArgument)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_two_arguments_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.voidMethodWithTwoArguments(GIVEN_STRING, GIVEN_INTEGER);
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
                .when(SystemUnderTest::voidMethodWithTwoArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_two_arguments_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        expect(systemUnderTestMock.nonVoidMethodWithTwoArguments(GIVEN_STRING, GIVEN_INTEGER)).andReturn(
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
                .when(SystemUnderTest::nonVoidMethodWithTwoArguments)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_three_arguments_given_a_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.voidMethodWithThreeArguments(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
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
                .when(SystemUnderTest::voidMethodWithThreeArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_three_arguments_given_a_non_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidMethodWithThreeArguments(GIVEN_STRING, GIVEN_INTEGER,
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
                .when(SystemUnderTest::nonVoidMethodWithThreeArguments)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithArgument(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(GIVEN_STRING)
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method_and_two_preparation_steps() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithArgument(GIVEN_STRING);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenArgument(GIVEN_STRING)
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithArgument(GIVEN_STRING)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(GIVEN_STRING)
                .when(SystemUnderTest::nonVoidMethodWithArgument)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_two_argument_values_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithTwoArguments(GIVEN_STRING, GIVEN_INTEGER);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(GIVEN_STRING)
                .andArgument(GIVEN_INTEGER)
                .when(SystemUnderTest::voidMethodWithTwoArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_three_argument_values_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithThreeArguments(GIVEN_STRING, GIVEN_INTEGER,
                GIVEN_BOOLEAN)).andReturn(EXPECTED_RESULT);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(GIVEN_STRING)
                .andArgument(GIVEN_INTEGER)
                .andArgument(GIVEN_BOOLEAN)
                .when(SystemUnderTest::nonVoidMethodWithThreeArguments)
                .then(result -> {
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }
}
