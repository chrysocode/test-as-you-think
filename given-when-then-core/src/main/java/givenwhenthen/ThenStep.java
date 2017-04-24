package givenwhenthen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import givenwhenthen.GivenWhenThenDsl.AndThen;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenFailure;

public class ThenStep<$SystemUnderTest, $Result>
        implements Then<$SystemUnderTest, $Result>, ThenFailure, AndThen<$SystemUnderTest, $Result> {

    private GivenWhenContext<$SystemUnderTest, $Result> steps;
    private $Result result;

    ThenStep(GivenWhenContext<$SystemUnderTest, $Result> steps) {
        this.steps = steps;
    }

    @Override
    public void then(Consumer<$Result> thenStep) {
        thenStep.accept(steps.returnResult());
    }

    @Override
    public void then(BiConsumer<$SystemUnderTest, $Result> thenStep) {
        thenStep.accept(steps.getSystemUnderTest(), steps.returnResult());
    }

    @Override
    public void then(Runnable thenStep) {
        steps.returnResult();
        thenStep.run();
    }

    @Override
    public void then(String expectationSpecification, Consumer<$Result> thenStep) {
        then(thenStep);
    }

    @Override
    public void then(String expectationSpecification, Runnable thenStep) {
        then(thenStep);
    }

    @Override
    public void then(Predicate<$Result> thenStep) {
        assertThat(thenStep.test(steps.returnResult())).isTrue();
    }

    @Override
    public void then(List<Predicate<$Result>> thenSteps) {
        assertThat(thenSteps.stream().reduce((predicate, another) -> predicate.and(another)).get()
                .test(steps.returnResult())).isTrue();
    }

    @Override
    public void then(BiPredicate<$SystemUnderTest, $Result> thenStep) {
        assertThat(thenStep.test(steps.getSystemUnderTest(), steps.returnResult())).isTrue();
    }

    @Override
    public void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        then(thenStepAboutResult);
        assertThat(thenStepAboutSystemUnderTest.test(steps.getSystemUnderTest())).isTrue();
    }

    @Override
    public void thenItFails() {
        assertThat(steps.returnResult()).isInstanceOf(Throwable.class);
    }

    @Override
    public void thenItFails(Class<? extends Throwable> expectedThrowableClass) {
        assertThat(steps.returnResult()).isInstanceOf(expectedThrowableClass);
    }

    @Override
    public void thenItFails(Class<? extends Throwable> expectedThrowableClass, String expectedMessage) {
        $Result result = steps.returnResult();
        assertThat(result).isInstanceOf(expectedThrowableClass);
        assertThat(((Throwable) result).getMessage()).isEqualTo(expectedMessage);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> thenMultipleExpectations(Consumer<$Result> thenStep) {
        result = steps.returnResult();
        thenStep.accept(result);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep) {
        thenStep.accept(result);
        return this;
    }
}
