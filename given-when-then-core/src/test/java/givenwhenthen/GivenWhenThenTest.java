package givenwhenthen;

import static givenwhenthen.GivenWhenThenTest.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

public class GivenWhenThenTest {

    private SystemUnderTest sut;

    @Before
    public void prepareFixtures() {
        sut = new SystemUnderTest();
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_non_void_method() {
        givenSut(sut) //
                .given(() -> {
                    fail("Not yet implemented!");
                }) //
                .when(sut -> {
                    return sut.nonVoidMethod();
                }) //
                .then(result -> {
                    fail("Not yet implemented!");
                });
    }

    @Test
    public void should_follow_the_given_when_then_full_sequence_given_a_void_method() {
        givenSut(sut) //
                .given(() -> {
                    fail("Not yet implemented!");
                }) //
                .when(sut -> {
                    sut.voidMethod();
                }) //
                .then((Void) -> {
                    fail("Not yet implemented!");
                });
    }

    public static class GivenWhenThen {

        public static <S> Given<S> givenSut(S systemUnderTest) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class Given<S> {

        public When<S> given(Runnable givenStep) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class When<S> {

        public <R> Then<R> when(Function<S, R> whenStep) {
            throw new RuntimeException("Not yet implemented!");
        }

        public Then<Void> when(Consumer<S> whenStep) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class Then<R> {

        public void then(Consumer<R> thenStep) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class SystemUnderTest {

        public String nonVoidMethod() {
            throw new RuntimeException("Not yet implemented!");
        }

        public void voidMethod() {
            throw new RuntimeException("Not yet implemented!");
        }
    }
}
