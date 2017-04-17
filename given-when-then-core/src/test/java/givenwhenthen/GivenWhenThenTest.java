package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSut;
import static givenwhenthen.GivenWhenThen.givenSutClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GivenWhenThenTest {

    private SystemUnderTest sut;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        replay(givenWhenThenDefinitionMock);

        sut = new SystemUnderTest(givenWhenThenDefinitionMock);
    }

    @After
    public void verifyMocks() {
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_non_void_method() {
        givenSut(sut) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }) //
                .when(sut -> {
                    return sut.nonVoidMethod();
                }) //
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_void_method() {
        givenSut(sut) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }) //
                .when(sut -> {
                    sut.voidMethod();
                }) //
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_sut_class_to_be_instanciated()
            throws Exception {
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    assertThat(result).isEqualTo("expected result");
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }
}
