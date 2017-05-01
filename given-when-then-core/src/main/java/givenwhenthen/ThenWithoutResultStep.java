package givenwhenthen;

import givenwhenthen.GivenWhenThenDsl.AndThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenWithoutResultStep<$SystemUnderTest> implements ThenWithoutResult<$SystemUnderTest>,
        AndThenWithoutResult<$SystemUnderTest> {

    private final GivenWhenContext<$SystemUnderTest, Void> context;

    public ThenWithoutResultStep(GivenWhenContext<$SystemUnderTest, Void> context) {
        this.context = context;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep) {
        context.returnResultOrVoid();
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Runnable thenStep) {
        context.returnResultOrVoid();
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(Consumer<$SystemUnderTest> thenStep) {
        context.returnResultOrVoid();
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(BooleanSupplier thenStep) {
        context.returnResultOrVoid();
        assertThat(thenStep.getAsBoolean()).isTrue();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(BooleanSupplier thenStep) {
        assertThat(thenStep.getAsBoolean()).isTrue();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Consumer<$SystemUnderTest>
            thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Consumer<$SystemUnderTest>
            thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }
}
