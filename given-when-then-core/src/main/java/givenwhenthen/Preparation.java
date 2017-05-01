package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final $SystemUnderTest systemUnderTest;
    private final List<Consumer<$SystemUnderTest>> givenSteps;

    Preparation($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        this.givenSteps = new ArrayList<>();
    }

    void recordGivenStep(Runnable givenStep) {
        givenSteps.add(functions.toConsumer(givenStep));
    }

    void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    void prepareFixtures() {
        givenSteps.stream().forEach(step -> step.accept(systemUnderTest));
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }
}