package givenwhenthen;

import java.util.function.Consumer;

public class Then<S, R> {

    private GivenWhenThenSteps<S, R> steps;

    Then(GivenWhenThenSteps<S, R> steps) {
        this.steps = steps;
    }

    public void then(Consumer<R> thenStep) {
        thenStep.accept(steps.returnResult());
    }
}
