package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingInput;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class WhenApplyingInputStep<$SystemUnderTest, $Input> implements WhenApplyingInput<$SystemUnderTest, $Input> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    WhenApplyingInputStep(Preparation<$SystemUnderTest> preparation) {this.preparation = preparation;}

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(BiConsumer<$SystemUnderTest, $Input> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), functions
                .toCheckedFunction(sut -> whenStep.accept(sut, preparation.supplyInput())));
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(BiFunction<$SystemUnderTest, $Input, $Result> whenStep) {
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), sut -> {
            return whenStep.apply(sut, preparation.supplyInput());
        });
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
