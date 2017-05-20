package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.PreparationStage.AndGivenArgument;
import givenwhenthen.GivenWhenThenDsl.PreparationStage.AndGivenTwoArguments;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.Then;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenFailure;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import givenwhenthen.function.CheckedBiConsumer;
import givenwhenthen.function.CheckedBiFunction;
import givenwhenthen.function.Functions;

import java.util.function.Supplier;

public class GivenArgumentWhenSteps<$SystemUnderTest, $Argument> implements AndGivenArgument<$SystemUnderTest,
        $Argument> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenArgumentWhenSteps(Preparation<$SystemUnderTest> preparation) {this.preparation = preparation;}

    @Override
    public <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(
            Supplier<$Argument2> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenTwoArgumentsWhenSteps<>(preparation);
    }

    @Override
    public <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(String description,
            Supplier<$Argument2> givenStep) {
        return andArgument(givenStep);
    }

    @Override
    public <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument(String description,
            $Argument2 argument) {
        preparation.recordGivenStep(functions.toSupplier(argument));
        return new GivenTwoArgumentsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedBiConsumer<$SystemUnderTest, $Argument> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            CheckedBiFunction<$SystemUnderTest, $Argument, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toFunction(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedBiConsumer<$SystemUnderTest, $Argument> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers())));
    }
}
