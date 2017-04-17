package givenwhenthen;

import java.util.function.Consumer;

public class Given<$SystemUnderTest> {

    private $SystemUnderTest systemUnderTest;
    private Consumer<$SystemUnderTest> givenStep;

    Given($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public When<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        this.givenStep = givenStep;
        return new When<>(this);
    }

    public When<$SystemUnderTest> given(Runnable givenStep) {
        this.givenStep = (sut) -> {
            givenStep.run();
        };
        return new When<>(this);
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    Consumer<$SystemUnderTest> getGivenStep() {
        return givenStep;
    }
}