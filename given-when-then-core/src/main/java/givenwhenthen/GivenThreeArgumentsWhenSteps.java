package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeArguments;

public class GivenThreeArgumentsWhenSteps<$SystemUnderTest, $Argument1, $Argument2, $Argument3> implements
        WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenThreeArgumentsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(
            QuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            QuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedFunction(whenStep, preparation.getArgumentSuppliers()));
    }
}
