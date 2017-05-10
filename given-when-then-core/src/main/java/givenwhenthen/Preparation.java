package givenwhenthen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final $SystemUnderTest systemUnderTest;
    private final List<Consumer<$SystemUnderTest>> givenSteps;
    private final Queue<Supplier> inputSuppliers;

    Preparation($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        givenSteps = new ArrayList<>();
        inputSuppliers = new LinkedList<>();
    }

    void recordGivenStep(Runnable givenStep) {
        givenSteps.add(functions.toConsumer(givenStep));
    }

    void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    <$Input> void recordGivenStep(Supplier<$Input> givenStep) {
        inputSuppliers.add(givenStep);
    }

    Queue<Supplier> getInputSuppliers() {
        return inputSuppliers;
    }

    void prepareFixtures() {
        givenSteps
                .stream()
                .forEach(step -> step.accept(systemUnderTest));
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }
}