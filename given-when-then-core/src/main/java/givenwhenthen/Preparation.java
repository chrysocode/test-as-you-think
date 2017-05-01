package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final $SystemUnderTest systemUnderTest;
    private final List<Consumer<$SystemUnderTest>> givenSteps;
    private Supplier<?> inputSupplier;

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

    <$Input> void recordGivenStep(Supplier<$Input> givenStep) {
        inputSupplier = givenStep;
    }

    <$Input> $Input supplyInput() {
        return ($Input) inputSupplier.get();
    }

    void prepareFixtures() {
        givenSteps.stream().forEach(step -> step.accept(systemUnderTest));
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }
}