package givenwhenthen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import givenwhenthen.GivenWhenThenDsl.AndGiven;
import givenwhenthen.GivenWhenThenDsl.Given;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenFailure;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;

public class GivenWhenSteps<$SystemUnderTest> implements Given<$SystemUnderTest> {

    private final $SystemUnderTest systemUnderTest;
    private final List<Consumer<$SystemUnderTest>> givenSteps;

    GivenWhenSteps($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        this.givenSteps = new ArrayList<>();
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
    public <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return toThenStep(whenStep);
    }

    private <$Result> Then<$SystemUnderTest, $Result> toThenStep(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(systemUnderTest);
        context.setGivenSteps(givenSteps);
        context.setWhenStep(whenStep);
        return new ThenStep<>(context);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep) {
        return toThenStep(whenStep);
    }

    private ThenWithoutResult<$SystemUnderTest> toThenStep(CheckedConsumer<$SystemUnderTest> whenStep) {
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(systemUnderTest);
        context.setGivenSteps(givenSteps);
        context.setWhenStep(sut -> {
            whenStep.accept(sut);
            return null;
        });
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep) {
        GivenWhenContext<$SystemUnderTest, Throwable> context = new GivenWhenContext<>(systemUnderTest);
        context.setGivenSteps(givenSteps);
        context.setWhenStep(sut -> {
            Throwable result = null;
            try {
                whenStep.accept(sut);
            } catch (Throwable throwable) {
                result = throwable;
            }
            return result;
        });
        return new ThenStep<>(context);
    }
}