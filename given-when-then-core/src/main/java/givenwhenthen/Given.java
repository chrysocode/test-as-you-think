package givenwhenthen;

public class Given<$SystemUnderTest> {

    private $SystemUnderTest systemUnderTest;
    private Runnable givenStep;

    Given($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public When<$SystemUnderTest> given(Runnable givenStep) {
        this.givenStep = givenStep;
        return new When<$SystemUnderTest>(this);
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    Runnable getGivenStep() {
        return givenStep;
    }
}