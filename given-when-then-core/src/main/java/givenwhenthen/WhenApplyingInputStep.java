package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingInput;

import java.util.function.BiConsumer;

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
}
