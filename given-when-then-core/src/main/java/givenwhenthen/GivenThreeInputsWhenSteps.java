package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

import java.util.function.BiFunction;

public class GivenThreeInputsWhenSteps<$SystemUnderTest, $Input1, $Input2, $Input3> implements
        WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> {

    private final Preparation<$SystemUnderTest> preparation;

    public GivenThreeInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(QuadriConsumer<$SystemUnderTest, $Input1, $Input2, $Input3>
                                                                whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), sut -> {
            whenStep.accept(sut, ($Input1) preparation.supplyInput(), ($Input2) preparation.supplyInput(), ($Input3)
                    preparation.supplyInput());
        });
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(QuadriFunction<$SystemUnderTest, $Input1, $Input2, $Input3,
            $Result> whenStep) {
        TriFunction<$SystemUnderTest, $Input1, $Input2, $Result> triFunction = (sut, input1, input2) -> whenStep
                .apply(sut, input1, input2, ($Input3) preparation.supplyInput());
        BiFunction<$SystemUnderTest, $Input1, $Result> biFunction = (sut, input1) -> triFunction.apply(sut, input1,
                ($Input2) preparation.supplyInput());
        CheckedFunction<$SystemUnderTest, $Result> elementaryWhenStep = sut -> biFunction.apply(sut, ($Input1)
                preparation.supplyInput());

        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), elementaryWhenStep);
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
