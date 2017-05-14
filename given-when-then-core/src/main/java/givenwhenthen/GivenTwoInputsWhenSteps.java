package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndGivenTwoInputs;
import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

import java.util.function.Supplier;

public class GivenTwoInputsWhenSteps<$SystemUnderTest, $Input1, $Input2> implements
        AndGivenTwoInputs<$SystemUnderTest, $Input1, $Input2> {

    private final Functions functions = Functions.INSTANCE;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    GivenTwoInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public <$Input3> WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> andInput(
            Supplier<$Input3> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenThreeInputsWhenSteps<>(preparation);
    }

    @Override
    public <$Input3> WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> andInput($Input3 input) {
        preparation.recordGivenStep(() -> input);
        return new GivenThreeInputsWhenSteps<>(preparation);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(TriConsumer<$SystemUnderTest, $Input1, $Input2> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedConsumer(whenStep, preparation.getInputSuppliers()));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(
            TriFunction<$SystemUnderTest, $Input1, $Input2, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation,
                functions.toCheckedFunction(whenStep, preparation.getInputSuppliers()));
    }
}
