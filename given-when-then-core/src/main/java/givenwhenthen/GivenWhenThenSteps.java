package givenwhenthen;

import java.util.function.Function;

class GivenWhenThenSteps<$SystemUnderTest, $Result> {

    private $SystemUnderTest systemUnderTest;
    private Runnable givenStep;
    private Function<$SystemUnderTest, $Result> whenStep;

    GivenWhenThenSteps($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    $Result returnResult() {
        givenStep.run();
        return whenStep.apply(systemUnderTest);
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    void setSystemUnderTest($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    Runnable getGivenStep() {
        return givenStep;
    }

    void setGivenStep(Runnable givenStep) {
        this.givenStep = givenStep;
    }

    Function<$SystemUnderTest, $Result> getWhenStep() {
        return whenStep;
    }

    void setWhenStep(Function<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}