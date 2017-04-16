package givenwhenthen;

import static givenwhenthen.GivenWhenThenTest.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.function.Consumer;
import java.util.function.Function;

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
            return new Given<>(systemUnderTest);
        }
    }

    public static class Given<S> {

        private S systemUnderTest;
        private Runnable givenStep;

        Given(S systemUnderTest) {
            this.systemUnderTest = systemUnderTest;
        }

        public When<S> given(Runnable givenStep) {
            this.givenStep = givenStep;
            return new When<S>(this);
        }

        S getSystemUnderTest() {
            return systemUnderTest;
        }

        Runnable getGivenStep() {
            return givenStep;
        }
    }

    static class GivenWhenThenSteps<S, R> {

        private S systemUnderTest;
        private Runnable givenStep;
        private Function<S, R> whenStep;

        GivenWhenThenSteps(S systemUnderTest) {
            this.systemUnderTest = systemUnderTest;
        }

        R returnResult() {
            givenStep.run();
            return whenStep.apply(systemUnderTest);
        }

        S getSystemUnderTest() {
            return systemUnderTest;
        }

        void setSystemUnderTest(S systemUnderTest) {
            this.systemUnderTest = systemUnderTest;
        }

        Runnable getGivenStep() {
            return givenStep;
        }

        void setGivenStep(Runnable givenStep) {
            this.givenStep = givenStep;
        }

        Function<S, R> getWhenStep() {
            return whenStep;
        }

        void setWhenStep(Function<S, R> whenStep) {
            this.whenStep = whenStep;
        }
    }

    public static class When<S> {

        private Given<S> given;

        When(Given<S> given) {
            this.given = given;
        }

        public <R> Then<S, R> when(Function<S, R> whenStep) {
            GivenWhenThenSteps<S, R> steps = new GivenWhenThenSteps<>(given.getSystemUnderTest());
            steps.setGivenStep(given.getGivenStep());
            steps.setWhenStep(whenStep);
            return new Then<>(steps);
        }

        public Then<S, Void> when(Consumer<S> whenStep) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class Then<S, R> {

        private GivenWhenThenSteps<S, R> steps;

        Then(GivenWhenThenSteps<S, R> steps) {
            this.steps = steps;
        }

        public void then(Consumer<R> thenStep) {
            thenStep.accept(steps.returnResult());
        }
    }

    public static class SystemUnderTest {

        public String nonVoidMethod() {
            return "expected result";
        }

        public void voidMethod() {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    public static class GivenWhenThenDefinition {

        public void givenAContextThatDefinesTheInitialStateOfTheSystem() {}

        public void whenAnEventHappensInRelationToAnActionOfTheConsumer() {}

        public void thenTheActualResultIsInKeepingWithTheExpectedResult() {}
    }
}
