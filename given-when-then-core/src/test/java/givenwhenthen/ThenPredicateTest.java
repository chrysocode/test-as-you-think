package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static java.util.Arrays.asList;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Test;

public class ThenPredicateTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    private void orderedSteps(int numberOfThenSteps) {
        // GIVEN
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        expectLastCall().times(numberOfThenSteps);

        replay(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_provide_a_then_step_as_a_predicate_given_a_non_void_method() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_provide_a_failing_then_step_as_a_predicate_given_a_non_void_method() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test
    public void should_provide_a_then_step_as_a_predicate_given_a_void_method() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    sut.voidMethod();
                }).then(Void -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_provide_a_failing_then_step_as_a_predicate_given_a_void_method() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    sut.voidMethod();
                }).then(Void -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test
    public void should_provide_the_then_steps_as_predicates_given_a_non_void_method() {
        // GIVEN
        orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(asList(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                } , result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }));
    }

    @Test(expected = ComparisonFailure.class)
    public void should_provide_the_then_steps_as_predicates_given_a_void_method_and_a_failing_then_step() {
        // GIVEN
        orderedSteps(2);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(asList(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                } , result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                }));
    }
}
