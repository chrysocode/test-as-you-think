package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

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
}
