package givenwhenthen;

import java.util.function.Consumer;

public class Then<$SystemUnderTest, $Result> implements GivenWhenThenDsl.ThenDsl<$SystemUnderTest, $Result> {

    private GivenWhenThenSteps<$SystemUnderTest, $Result> steps;

    Then(GivenWhenThenSteps<$SystemUnderTest, $Result> steps) {
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
}
