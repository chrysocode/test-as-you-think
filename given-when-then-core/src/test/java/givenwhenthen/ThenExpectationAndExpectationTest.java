package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class ThenExpectationAndExpectationTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            expectLastCall().times(2);
        }
        replay(givenWhenThenDefinitionMock);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_expectations_separately_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).startsWith("expected");
                }).and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).endsWith("result");
                });
    }
}
