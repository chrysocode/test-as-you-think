package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenTwoArguments;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeArguments;
import givenwhenthen.function.CheckedTriConsumer;
import givenwhenthen.function.CheckedTriFunction;
import givenwhenthen.function.Functions;

import java.util.function.Supplier;

public class GivenTwoArgumentsWhenSteps<$SystemUnderTest, $Argument1, $Argument2> implements
        AndGivenTwoArguments<$SystemUnderTest, $Argument1, $Argument2> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenTwoArgumentsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
            Supplier<$Argument3> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenThreeArgumentsWhenSteps<>(preparation);
    }

    @Override
    public <$Argument3> WhenApplyingThreeArguments<$SystemUnderTest, $Argument1, $Argument2, $Argument3> andArgument(
            $Argument3 argument) {
        preparation.recordGivenStep(functions.toSupplier(argument));
        return new GivenThreeArgumentsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(
            CheckedTriConsumer<$SystemUnderTest, $Argument1, $Argument2> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            CheckedTriFunction<$SystemUnderTest, $Argument1, $Argument2, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toFunction(whenStep, preparation.getArgumentSuppliers()));
    }
}
