package testasyouthink;

import org.junit.Test;
import testasyouthink.fixture.SystemUnderTest;

import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenSutRepliesWithinTimeLimitTest {

    @Test
    public void should_fail_given_a_too_slow_void_method() {
        assertThatThrownBy(() -> givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    sleep(1000);
                })
                .thenSutRepliesWithin(1))
                .isInstanceOf(AssertionError.class)
                .hasMessage("test timed out after 1 milliseconds")
                .hasCauseInstanceOf(TimeoutException.class);
    }
}
