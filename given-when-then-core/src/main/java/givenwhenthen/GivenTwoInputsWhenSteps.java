package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenTwoInputs;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

import java.util.function.Supplier;

public class GivenTwoInputsWhenSteps<$SystemUnderTest, $Input1, $Input2> implements
        AndGivenTwoInputs<$SystemUnderTest, $Input1, $Input2> {

    private final Functions functions = Functions.INSTANCE;
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
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), functions
                .toCheckedFunction(whenStep, preparation.getInputSuppliers()));
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
