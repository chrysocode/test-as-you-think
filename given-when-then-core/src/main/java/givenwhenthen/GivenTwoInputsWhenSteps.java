package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingTwoInputs;

public class GivenTwoInputsWhenSteps<$SystemUnderTest, $Input1, $Input2> implements
        WhenApplyingTwoInputs<$SystemUnderTest, $Input1, $Input2> {

    private final Preparation<$SystemUnderTest> preparation;

    GivenTwoInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(TriConsumer<$SystemUnderTest, $Input1, $Input2> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), sut -> {
            whenStep.accept(sut, ($Input1) preparation.supplyInput(), ($Input2) preparation.supplyInput());
        });
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }
}
