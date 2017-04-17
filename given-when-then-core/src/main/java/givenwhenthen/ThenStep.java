package givenwhenthen;

import java.util.function.Consumer;

public class ThenStep<$SystemUnderTest, $Result> implements GivenWhenThenDsl.Then<$SystemUnderTest, $Result> {

    private GivenWhenSteps<$SystemUnderTest, $Result> steps;

    ThenStep(GivenWhenSteps<$SystemUnderTest, $Result> steps) {
        this.steps = steps;
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
    public void then(String expectationSpecification, Consumer<$Result> thenStep) {
        then(thenStep);
    }
}
