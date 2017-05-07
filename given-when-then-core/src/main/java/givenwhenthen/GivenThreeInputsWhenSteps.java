package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

public class GivenThreeInputsWhenSteps<$SystemUnderTest, $Input1, $Input2, $Input3> implements
        WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenThreeInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(QuadriConsumer<$SystemUnderTest, $Input1, $Input2, $Input3>
                                                                whenStep) {
        return thenStepFactory.createThenWithoutResultStep(preparation, functions.toCheckedConsumer(whenStep,
                preparation.getInputSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(QuadriFunction<$SystemUnderTest, $Input1, $Input2, $Input3,
            $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toCheckedFunction(whenStep, preparation
                .getInputSuppliers()));
    }
}
