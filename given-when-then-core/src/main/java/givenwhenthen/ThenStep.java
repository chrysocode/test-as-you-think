package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import givenwhenthen.GivenWhenThenDsl.AndThen;

public class ThenStep<$SystemUnderTest, $Result> implements GivenWhenThenDsl.Then<$SystemUnderTest, $Result> {

    private GivenWhenSteps<$SystemUnderTest, $Result> steps;
    private List<Consumer<$Result>> thenSteps;

    ThenStep(GivenWhenSteps<$SystemUnderTest, $Result> steps) {
        this.steps = steps;
        this.thenSteps = new ArrayList<>();
    }

    @Override
    public void then(Consumer<$Result> thenStep) {
        thenStep.accept(steps.returnResult());
    }

    @Override
    public void then(Runnable thenStep) {
        steps.returnResult();
        thenStep.run();
    }

    @Override
    public void then(String expectationSpecification, Runnable thenStep) {
        then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep) {
        thenSteps.add(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep) {
        then(expectationSpecification, thenStep);
        return this;
    }

    @Override
    public void go() {
        $Result result = steps.returnResult();
        thenSteps.stream().forEach(step -> step.accept(result));
    }
}
