package givenwhenthen;

import givenwhenthen.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.GivenWhenThenDefinition.orderedSteps;
import static org.easymock.EasyMock.verify;

public class GivenInputTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_a_method_reference_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(SystemUnderTest::voidMethod) //
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_a_method_reference_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(SystemUnderTest::nonVoidMethod) //
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_receive_an_input_argument_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps();

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock)) //
                .andInput(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given input";
                }).when(SystemUnderTest::voidMethodWithArgument) //
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }
}
