package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Preparation<$SystemUnderTest> {

    private final $SystemUnderTest systemUnderTest;
    private final List<Consumer<$SystemUnderTest>> givenSteps;

    Preparation($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        this.givenSteps = new ArrayList<>();
    }

    void recordGivenStep(Runnable givenStep) {
        givenSteps.add(toConsumer(givenStep));
    }

    private Consumer<$SystemUnderTest> toConsumer(Runnable givenStep) {
        return sut -> {
            givenStep.run();
        };
    }

    void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    List<Consumer<$SystemUnderTest>> getGivenSteps() {
        return givenSteps;
    }
}