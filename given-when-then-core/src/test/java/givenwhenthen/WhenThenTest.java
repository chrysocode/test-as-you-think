package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WhenThenTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        replay(givenWhenThenDefinitionMock);
    }

    @After
    public void verifyMocks() {
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_non_void_method() {
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock)) //
                .when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
        ;
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_void_method() {
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock)) //
                .when(sut -> {
                    sut.voidMethod();
                }).then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }
}