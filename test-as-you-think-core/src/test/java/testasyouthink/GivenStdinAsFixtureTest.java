package testasyouthink;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenStdinAsFixtureTest {

    @Test
    void should_prepare_stdin_to_read_a_message() {
        // GIVEN
        GivenWhenThenDefinition gwtMock = Mockito.mock(GivenWhenThenDefinition.class);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    String givenInputMessage = "expected";
                    InputStream stdinFake = new ByteArrayInputStream(givenInputMessage.getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    String actualMessage = stdin.readLine();
                    stdin.close();
                    System.out.println(String.format("\nMessage: %s", actualMessage));
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualMessage;
                })
                .then(result -> {
                    assertThat(result).isEqualTo("expected");
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        InOrder inOrder = inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .givenAContextThatDefinesTheInitialStateOfTheSystem();
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock)
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }
}
