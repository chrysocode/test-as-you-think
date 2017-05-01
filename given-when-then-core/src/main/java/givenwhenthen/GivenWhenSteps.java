package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGiven;
import givenwhenthen.GivenWhenThenDsl.Given;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenFailure;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;

import java.util.function.Consumer;

public class GivenWhenSteps<$SystemUnderTest> implements Given<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenWhenSteps($SystemUnderTest systemUnderTest) {
        preparation = new Preparation<>(systemUnderTest);
    }

    @Override
    public Given<$SystemUnderTest> given(Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public Given<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public Given<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public Given<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
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
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), whenStep);
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep) {
        return toThenStep(whenStep);
    }

    private ThenWithoutResult<$SystemUnderTest> toThenStep(CheckedConsumer<$SystemUnderTest> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), whenStep);
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep) {
        Event<$SystemUnderTest, Throwable> event = new Event<>(preparation.getSystemUnderTest(), functions
                .toCheckedFunctionWithThrowableAsResult(whenStep));
        GivenWhenContext<$SystemUnderTest, Throwable> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}