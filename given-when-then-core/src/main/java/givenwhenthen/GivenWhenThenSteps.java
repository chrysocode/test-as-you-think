package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

class GivenWhenThenSteps<$SystemUnderTest, $Result> {

    private $SystemUnderTest systemUnderTest;
    private Consumer<$SystemUnderTest> givenStep;
    private Function<$SystemUnderTest, $Result> whenStep;

    GivenWhenThenSteps($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    $Result returnResult() {
        givenStep.accept(systemUnderTest);
        return whenStep.apply(systemUnderTest);
    }

    void setGivenStep(Consumer<$SystemUnderTest> givenStep) {
        this.givenStep = givenStep;
    }

    void setWhenStep(Function<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}