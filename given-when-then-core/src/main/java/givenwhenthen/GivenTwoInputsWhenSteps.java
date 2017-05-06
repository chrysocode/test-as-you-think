package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenTwoInputs;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GivenTwoInputsWhenSteps<$SystemUnderTest, $Input1, $Input2> implements
        AndGivenTwoInputs<$SystemUnderTest, $Input1, $Input2> {

    private final Preparation<$SystemUnderTest> preparation;

    GivenTwoInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public <$Input3> WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> andInput(Supplier<$Input3>
                                                                                                               givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenThreeInputsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(TriConsumer<$SystemUnderTest, $Input1, $Input2> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), sut -> {
            whenStep.accept(sut, ($Input1) preparation.supplyInput(), ($Input2) preparation.supplyInput());
        });
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(TriFunction<$SystemUnderTest, $Input1, $Input2, $Result>
                                                                      whenStep) {
        BiFunction<$SystemUnderTest, $Input1, $Result> biFunction = (sut, input1) -> whenStep.apply(sut, input1,
                ($Input2) preparation.supplyInput());
        CheckedFunction<$SystemUnderTest, $Result> elementaryWhenStep = sut -> biFunction.apply(sut, ($Input1)
                preparation.supplyInput());

        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), elementaryWhenStep);
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
