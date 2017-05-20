package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.VerificationStage.AndThen;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.Then;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenFailure;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenFailureWithExpectedException;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenFailureWithExpectedMessage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenStep<$SystemUnderTest, $Result> implements Then<$SystemUnderTest, $Result>,
        AndThen<$SystemUnderTest, $Result>, ThenFailure, ThenFailureWithExpectedException,
        ThenFailureWithExpectedMessage {

    private final GivenWhenContext<$SystemUnderTest, $Result> context;
    private $Result result;

    ThenStep(GivenWhenContext<$SystemUnderTest, $Result> context) {
        this.context = context;
    }

    private $Result result() {
        return result == null ? result = context.returnResultOrVoid() : result;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep) {
        thenStep.accept(result());
        return this;
    }

    @Override
    public void then(BiConsumer<$SystemUnderTest, $Result> thenStep) {
        thenStep.accept(context.getSystemUnderTest(), context.returnResultOrVoid());
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Runnable thenStep) {
        result();
        thenStep.run();
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep) {
        then(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Runnable thenStep) {
        then(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Predicate<$Result> thenStep) {
        assertThat(thenStep.test(result())).isTrue();
        return this;
    }

    @Override
    public void then(List<Predicate<$Result>> thenSteps) {
        assertThat(thenSteps
                .stream()
                .reduce((predicate, another) -> predicate.and(another))
                .get()
                .test(context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(BiPredicate<$SystemUnderTest, $Result> thenStep) {
        assertThat(thenStep.test(context.getSystemUnderTest(), context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        then(thenStepAboutResult);
        assertThat(thenStepAboutSystemUnderTest.test(context.getSystemUnderTest())).isTrue();
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep) {
        thenStep.accept(result());
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Predicate<$Result> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Runnable thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public ThenFailureWithExpectedException thenItFails() {
        assertThat(context.returnResultOrVoid()).isInstanceOf(Throwable.class);
        return this;
    }

    @Override
    public ThenFailureWithExpectedMessage byThrowing(Class<? extends Throwable> expectedThrowableClass) {
        assertThat(context.returnResultOrVoid()).isInstanceOf(expectedThrowableClass);
        return this;
    }

    @Override
    public void withMessage(String expectedMessage) {
        assertThat(((Throwable) context.returnResultOrVoid()).getMessage()).isEqualTo(expectedMessage);
    }
}
