package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSut;
import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class GivenArgumentsTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Test
    public void should_receive_an_argument_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_an_argument_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
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
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_two_arguments_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
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
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_two_arguments_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
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
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_three_arguments_given_a_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock = orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
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
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_three_arguments_given_a_non_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock = orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
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
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method() {
        //GIVEN
        IMocksControl mocksControl = createStrictControl();
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
        SystemUnderTest systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
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
        IMocksControl mocksControl = createStrictControl();
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        SystemUnderTest systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
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
        IMocksControl mocksControl = createStrictControl();
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
        SystemUnderTest systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
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
        IMocksControl mocksControl = createStrictControl();
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
        SystemUnderTest systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
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
        IMocksControl mocksControl = createStrictControl();
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
        SystemUnderTest systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
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
