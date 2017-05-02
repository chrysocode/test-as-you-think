package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenInput;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingTwoInputs;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GivenInputWhenSteps<$SystemUnderTest, $Input> implements AndGivenInput<$SystemUnderTest, $Input> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenInputWhenSteps(Preparation<$SystemUnderTest> preparation) {this.preparation = preparation;}

    @Override
    public <$Input2> WhenApplyingTwoInputs<$SystemUnderTest, $Input, $Input2> andInput(Supplier<$Input2> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenTwoInputsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(BiConsumer<$SystemUnderTest, $Input> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), functions
                .toCheckedFunction(sut -> whenStep.accept(sut, ($Input) preparation.supplyInput())));
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(BiFunction<$SystemUnderTest, $Input, $Result> whenStep) {
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), sut -> {
            return whenStep.apply(sut, ($Input) preparation.supplyInput());
        });
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
