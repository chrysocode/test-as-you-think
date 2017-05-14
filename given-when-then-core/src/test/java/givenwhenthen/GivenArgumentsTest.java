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
        systemUnderTestMock.voidMethodWithArgument("given argument");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
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
        expect(systemUnderTestMock.nonVoidMethodWithArgument("given argument")).andReturn("expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .and("specified fixture",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .when(SystemUnderTest::nonVoidMethodWithArgument)
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
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
        systemUnderTestMock.voidMethodWithTwoArguments("given argument", 20170502);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
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
        expect(systemUnderTestMock.nonVoidMethodWithTwoArguments("given argument", 20170502)).andReturn(
                "expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .when(SystemUnderTest::nonVoidMethodWithTwoArguments)
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
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
        systemUnderTestMock.voidMethodWithThreeArguments("given argument", 20170502, true);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return true;
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
        expect(systemUnderTestMock.nonVoidMethodWithThreeArguments("given argument", 20170502, true)).andReturn(
                "expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return true;
                })
                .when(SystemUnderTest::nonVoidMethodWithThreeArguments)
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithArgument("given argument");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("given argument")
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method_and_two_preparation_steps() {
        //GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.voidMethodWithArgument("given argument");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenArgument("given argument")
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_an_argument_value_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithArgument("given argument")).andReturn("expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("given argument")
                .when(SystemUnderTest::nonVoidMethodWithArgument)
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_two_argument_values_given_a_void_method() {
        //GIVEN
        systemUnderTestMock.voidMethodWithTwoArguments("given argument", 20170513);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("given argument")
                .andArgument(20170513)
                .when(SystemUnderTest::voidMethodWithTwoArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }

    @Test
    public void should_receive_three_argument_values_given_a_non_void_method() {
        //GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithThreeArguments("given argument", 20170514, false)).andReturn(
                "expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument("given argument")
                .andArgument(20170514)
                .andArgument(false)
                .when(SystemUnderTest::nonVoidMethodWithThreeArguments)
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        mocksControl.verify();
    }
}
