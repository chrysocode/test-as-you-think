package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenArgument;
import givenwhenthen.GivenWhenThenDsl.AndGivenTwoArguments;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
    public <$Argument2> AndGivenTwoArguments<$SystemUnderTest, $Argument, $Argument2> andArgument($Argument2 argument) {
        preparation.recordGivenStep(functions.toSupplier(argument));
        return new GivenTwoArgumentsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(BiConsumer<$SystemUnderTest, $Argument> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedConsumer(whenStep, preparation.getArgumentSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(BiFunction<$SystemUnderTest, $Argument, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedFunction(whenStep, preparation.getArgumentSuppliers()));
    }
}
