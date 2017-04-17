package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

import givenwhenthen.GivenWhenThenDsl.Given;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.When;

public class GivenWhenThen<$SystemUnderTest> implements Given<$SystemUnderTest>, When<$SystemUnderTest> {

    private $SystemUnderTest systemUnderTest;
    private Consumer<$SystemUnderTest> givenStep;

    private GivenWhenThen($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
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
    public When<$SystemUnderTest> given(Runnable givenStep) {
        this.givenStep = sut -> {
            givenStep.run();
        };
        return this;
    }

    @Override
    public When<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        this.givenStep = givenStep;
        return this;
    }

    @Override
    public When<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep) {
        given(givenStep);
        return this;
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
        steps.setGivenStep(givenStep);
        steps.setWhenStep(whenStep);
        return new ThenStep<>(steps);
    }
}
