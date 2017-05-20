package testasyouthink;

import testasyouthink.function.Functions;

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
    private final Queue<Supplier> argumentSuppliers;

    Preparation($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        givenSteps = new ArrayList<>();
        argumentSuppliers = new LinkedList<>();
    }

    void recordGivenStep(Runnable givenStep) {
        givenSteps.add(functions.toConsumer(givenStep));
    }

    void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    <$Argument> void recordGivenStep(Supplier<$Argument> givenStep) {
        argumentSuppliers.add(givenStep);
    }

    Queue<Supplier> getArgumentSuppliers() {
        return argumentSuppliers;
    }

    void prepareFixtures() {
        givenSteps.forEach(step -> step.accept(systemUnderTest));
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }
}