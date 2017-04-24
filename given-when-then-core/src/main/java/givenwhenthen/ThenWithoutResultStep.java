package givenwhenthen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import givenwhenthen.GivenWhenThenDsl.AndThenWithoutResult;
import givenwhenthen.GivenWhenThenDsl.ThenWithoutResult;

public class ThenWithoutResultStep<$SystemUnderTest>
        implements ThenWithoutResult<$SystemUnderTest>, AndThenWithoutResult<$SystemUnderTest> {

    private GivenWhenContext<$SystemUnderTest, Void> context;

    public ThenWithoutResultStep(GivenWhenContext<$SystemUnderTest, Void> context) {
        this.context = context;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep) {
        context.returnResult();
        thenStep.run();
        return this;
    }

    @Override
    public void then(String expectationSpecification, Runnable thenStep) {
        context.returnResult();
        thenStep.run();
    }

    @Override
    public void then(Consumer<$SystemUnderTest> thenStep) {
        context.returnResult();
        thenStep.accept(context.getSystemUnderTest());
    }

    @Override
    public void then(BooleanSupplier thenStep) {
        context.returnResult();
        assertThat(thenStep.getAsBoolean()).isTrue();
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep) {
        thenStep.run();
        return this;
    }
}
