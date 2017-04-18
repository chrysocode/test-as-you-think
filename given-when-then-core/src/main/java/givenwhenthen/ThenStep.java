package givenwhenthen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import givenwhenthen.GivenWhenThenDsl.Then;

public class ThenStep<$SystemUnderTest, $Result> implements Then<$SystemUnderTest, $Result> {

    private GivenWhenSteps<$SystemUnderTest, $Result> steps;

    ThenStep(GivenWhenSteps<$SystemUnderTest, $Result> steps) {
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
}
