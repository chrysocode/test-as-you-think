package givenwhenthen;

public class Given<S> {

    private S systemUnderTest;
    private Runnable givenStep;

    Given(S systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public When<S> given(Runnable givenStep) {
        this.givenStep = givenStep;
        return new When<S>(this);
    }

    S getSystemUnderTest() {
        return systemUnderTest;
    }

    Runnable getGivenStep() {
        return givenStep;
    }
}