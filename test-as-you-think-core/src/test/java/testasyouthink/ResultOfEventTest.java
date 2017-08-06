package testasyouthink;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.resultOf;

public class ResultOfEventTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultOfEventTest.class);
    private GivenWhenThenDefinition gwtMock;

    @Before
    public void prepareFixtures() {
        gwtMock = mock(GivenWhenThenDefinition.class);
    }

    @Test
    public void should_verify_an_actual_string_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "expected result";
        })
                .isEqualTo("expected result")
                .contains("result")).hasSameClassAs(assertThat(""));
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_fail_to_verify_an_actual_string_is_conform_to_an_expected_result() {
        // WHEN
        Throwable thrown = catchThrowable(() -> resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "actual";
        }).isEqualTo("expected"));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }
}
