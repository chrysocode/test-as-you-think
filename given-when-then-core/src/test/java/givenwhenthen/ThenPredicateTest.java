package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class ThenPredicateTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        }
        replay(givenWhenThenDefinitionMock);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_provide_then_step_as_a_predicate_given_a_non_void_method() {
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

    @Test
    public void should_provide_then_step_as_a_predicate_given_a_void_method() {
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    sut.voidMethod();
                }).then((Void) -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }
}
