package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.PreparationStage.AndGiven;
import givenwhenthen.GivenWhenThenDsl.PreparationStage.AndGivenArgument;
import givenwhenthen.GivenWhenThenDsl.PreparationStage.Given;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.Then;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenFailure;
import givenwhenthen.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import givenwhenthen.function.CheckedConsumer;
import givenwhenthen.function.CheckedFunction;
import givenwhenthen.function.Functions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GivenWhenSteps<$SystemUnderTest> implements Given<$SystemUnderTest>, AndGiven<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;

    GivenWhenSteps($SystemUnderTest systemUnderTest) {
        preparation = new Preparation<>(systemUnderTest);
    }

    @Override
    public AndGiven<$SystemUnderTest> given(Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(Supplier<$Argument> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenArgumentWhenSteps<>(preparation);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            Supplier<$Argument> givenStep) {
        return givenArgument(givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            $Argument argument) {
        preparation.recordGivenStep(functions.toSupplier(argument));
        return new GivenArgumentWhenSteps<>(preparation);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(whenStep));
    }
}