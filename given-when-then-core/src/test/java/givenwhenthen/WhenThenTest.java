package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import givenwhenthen.fixture.SystemUnderTest;

@SuppressWarnings("unused")
public class WhenThenTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
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
    public void should_follow_the_when_then_partial_sequence_given_a_non_void_method() {
        // WHEN
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
        // WHEN
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock)) //
                .when(sut -> {
                    sut.voidMethod();
                }).then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_sut_class_to_be_instantiated() {
        // WHEN
        GivenWhenThen.givenSutClass(SystemUnderTest.class) //
                .when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }
}