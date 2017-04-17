package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import givenwhenthen.GivenWhenThenDsl.AndGiven;
import givenwhenthen.GivenWhenThenDsl.Given;
import givenwhenthen.GivenWhenThenDsl.Then;

public class GivenWhenThen<$SystemUnderTest> implements Given<$SystemUnderTest> {

    private $SystemUnderTest systemUnderTest;
    private List<Consumer<$SystemUnderTest>> givenSteps;

    private GivenWhenThen($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        this.givenSteps = new ArrayList<>();
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new GivenWhenThen<>(systemUnderTest);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass) {
        try {
            return new GivenWhenThen<$SystemUnderTest>(sutClass.newInstance());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Given<$SystemUnderTest> given(Runnable givenStep) {
        this.givenSteps.add(sut -> {
            givenStep.run();
        });
        return this;
    }

    @Override
    public Given<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        this.givenSteps.add(givenStep);
        return this;
    }

    @Override
    public Given<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep) {
        return given(givenStep);
    }

    @Override
    public Given<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        return given(givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(Function<$SystemUnderTest, $Result> whenStep) {
        return toThenStep(whenStep);
    }

    @Override
    public Then<$SystemUnderTest, Void> when(Consumer<$SystemUnderTest> whenStep) {
        return toThenStep(sut -> {
            whenStep.accept(sut);
            return null;
        });
    }

    private <$Result> Then<$SystemUnderTest, $Result> toThenStep(Function<$SystemUnderTest, $Result> whenStep) {
        GivenWhenSteps<$SystemUnderTest, $Result> steps = new GivenWhenSteps<>(systemUnderTest);
        steps.setGivenSteps(givenSteps);
        steps.setWhenStep(whenStep);
        return new ThenStep<>(steps);
    }
}
