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

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    void setSystemUnderTest($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    Consumer<$SystemUnderTest> getGivenStep() {
        return givenStep;
    }

    void setGivenStep(Consumer<$SystemUnderTest> givenStep) {
        this.givenStep = givenStep;
    }

    Function<$SystemUnderTest, $Result> getWhenStep() {
        return whenStep;
    }

    void setWhenStep(Function<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}