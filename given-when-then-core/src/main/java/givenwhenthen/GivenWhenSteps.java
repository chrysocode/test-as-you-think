package givenwhenthen;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class GivenWhenSteps<$SystemUnderTest, $Result> {

    private $SystemUnderTest systemUnderTest;
    private List<Consumer<$SystemUnderTest>> givenSteps;
    private Function<$SystemUnderTest, $Result> whenStep;

    GivenWhenSteps($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    $Result returnResult() {
        if (givenSteps != null && !givenSteps.isEmpty()) {
            givenSteps.stream().forEach(step -> step.accept(systemUnderTest));
        }
        return whenStep.apply(systemUnderTest);
    }

    void setGivenSteps(List<Consumer<$SystemUnderTest>> givenSteps) {
        this.givenSteps = givenSteps;
    }

    void setWhenStep(Function<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}