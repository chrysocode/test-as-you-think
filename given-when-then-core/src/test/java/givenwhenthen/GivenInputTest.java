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

public class GivenInputTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Test
    public void should_receive_an_input_argument_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                })
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_an_input_argument_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .and("specified fixture",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
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
    public void should_receive_two_input_arguments_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                })
                .andInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .when(SystemUnderTest::voidMethodWithTwoArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_two_input_arguments_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                })
                .andInput(() -> {
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
    public void should_receive_three_input_arguments_given_a_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock = orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                })
                .andInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .andInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return true;
                })
                .when(SystemUnderTest::voidMethodWithThreeArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_three_input_arguments_given_a_non_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock = orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock))
                .givenInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                })
                .andInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 20170502;
                })
                .andInput(() -> {
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
        systemUnderTestMock.voidMethodWithArgument("given input");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenInput("given input")
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
        systemUnderTestMock.voidMethodWithArgument("given input");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .given(() -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .givenInput("given input")
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
        expect(systemUnderTestMock.nonVoidMethodWithArgument("given input")).andReturn("expected result");
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenInput("given input")
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
        systemUnderTestMock.voidMethodWithTwoArguments("given input", 20170513);
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenInput("given input")
                .andInput(20170513)
                .when(SystemUnderTest::voidMethodWithTwoArguments)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());

        // THEN
        mocksControl.verify();
    }
}
