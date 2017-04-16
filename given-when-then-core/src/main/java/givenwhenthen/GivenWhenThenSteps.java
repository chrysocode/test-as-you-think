package givenwhenthen;

import java.util.function.Function;

class GivenWhenThenSteps<S, R> {

    private S systemUnderTest;
    private Runnable givenStep;
    private Function<S, R> whenStep;

    GivenWhenThenSteps(S systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    R returnResult() {
        givenStep.run();
        return whenStep.apply(systemUnderTest);
    }

    S getSystemUnderTest() {
        return systemUnderTest;
    }

    void setSystemUnderTest(S systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    Runnable getGivenStep() {
        return givenStep;
    }

    void setGivenStep(Runnable givenStep) {
        this.givenStep = givenStep;
    }

    Function<S, R> getWhenStep() {
        return whenStep;
    }

    void setWhenStep(Function<S, R> whenStep) {
        this.whenStep = whenStep;
    }
}