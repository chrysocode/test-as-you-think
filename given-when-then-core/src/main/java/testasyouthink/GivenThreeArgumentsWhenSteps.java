package testasyouthink;

import testasyouthink.GivenWhenThenDsl.ExecutionStage.WhenApplyingThreeArguments;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedQuadriConsumer;
import testasyouthink.function.CheckedQuadriFunction;
import testasyouthink.function.Functions;

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
            CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            CheckedQuadriFunction<$SystemUnderTest, $Argument1, $Argument2, $Argument3, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toFunction(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(
            CheckedQuadriConsumer<$SystemUnderTest, $Argument1, $Argument2, $Argument3> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers())));
    }
}
