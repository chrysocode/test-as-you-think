package givenwhenthen;

import java.util.function.Consumer;

public class Then<$SystemUnderTest, $Result> {

    private GivenWhenThenSteps<$SystemUnderTest, $Result> steps;

    Then(GivenWhenThenSteps<$SystemUnderTest, $Result> steps) {
        this.steps = steps;
    }

    public void then(Consumer<$Result> thenStep) {
        thenStep.accept(steps.returnResult());
    }
}
