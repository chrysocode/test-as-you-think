package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.Then;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.WhenApplyingThreeInputs;

import java.util.Queue;
import java.util.function.Supplier;

public class GivenThreeInputsWhenSteps<$SystemUnderTest, $Input1, $Input2, $Input3> implements
        WhenApplyingThreeInputs<$SystemUnderTest, $Input1, $Input2, $Input3> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;

    public GivenThreeInputsWhenSteps(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(QuadriConsumer<$SystemUnderTest, $Input1, $Input2, $Input3>
                                                                whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), toCheckedConsumer
                (whenStep, preparation.getInputSuppliers()));
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }

    private TriConsumer<$SystemUnderTest, $Input1, $Input2> toTriConsumer(QuadriConsumer<$SystemUnderTest, $Input1,
            $Input2, $Input3> whenStep, Queue<Supplier> suppliers) {
        return (sut, input1, input2) -> whenStep.accept(sut, input1, input2, ($Input3) suppliers
                .remove()
                .get());
    }

    private CheckedConsumer<$SystemUnderTest> toCheckedConsumer(QuadriConsumer<$SystemUnderTest, $Input1, $Input2,
            $Input3> whenStep, Queue<Supplier> suppliers) {
        return functions.toCheckedConsumer(functions.toBiConsumer(toTriConsumer(whenStep, suppliers), suppliers),
                suppliers);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(QuadriFunction<$SystemUnderTest, $Input1, $Input2, $Input3,
            $Result> whenStep) {
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), functions
                .toCheckedFunction(whenStep, preparation.getInputSuppliers()));
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }
}
