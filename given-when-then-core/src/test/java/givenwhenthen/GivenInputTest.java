package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

public class GivenInputTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

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
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
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
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
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
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }

    @Test
    public void should_receive_an_argument_value_given_a_void_method() {
        //GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .givenInput("given input")
                .when(SystemUnderTest::voidMethodWithArgument)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }
}
