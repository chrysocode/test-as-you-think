package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

public class GivenWhenThenTest {

    private SystemUnderTest sut;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        sut = new SystemUnderTest();

        givenWhenThenDefinitionMock = mock(GivenWhenThenDefinition.class);
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        replay(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_non_void_method() {
        // WHEN
        givenSut(sut) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }) //
                .when(sut -> {
                    return sut.nonVoidMethod();
                }) //
                .then(result -> {
                    assertThat(result).isEqualTo("expected result");
                });

        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_void_method() {
        // WHEN
        givenSut(sut) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }) //
                .when(sut -> {
                    sut.voidMethod();
                }) //
                .then((Void) -> {
                    assertThat(sut.getVoidMethodResult()).isEqualTo("expected result");
                });

        // THEN
        verify(givenWhenThenDefinitionMock);
    }
}
