package testasyouthink;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenStdoutAsResultTest {

    @Test
    public void should_verify_the_standard_output_as_a_result_given_a_void_target_method() {
        // GIVEN
        GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStdoutToBeCaptured()
                .when(sut -> {
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    System.out.println("Stdout as a result");
                })
                .thenStdoutAsResult(result -> {
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).hasContent("Stdout as a result");
                });

        // THEN
        InOrder inOrder = Mockito.inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock)
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }
}
