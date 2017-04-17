package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

import givenwhenthen.GivenWhenThenDsl.GivenDsl;
import givenwhenthen.GivenWhenThenDsl.ThenDsl;
import givenwhenthen.GivenWhenThenDsl.WhenDsl;

public class GivenWhenThen<$SystemUnderTest> implements GivenDsl<$SystemUnderTest>, WhenDsl<$SystemUnderTest> {

    private $SystemUnderTest systemUnderTest;
    private Consumer<$SystemUnderTest> givenStep;

    private GivenWhenThen($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public static <$SystemUnderTest> GivenDsl<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new GivenWhenThen<>(systemUnderTest);
    }

    public static <$SystemUnderTest> GivenDsl<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass)
            throws InstantiationException, IllegalAccessException {
        return new GivenWhenThen<$SystemUnderTest>(sutClass.newInstance());
    }

    @Override
    public WhenDsl<$SystemUnderTest> given(Runnable givenStep) {
        this.givenStep = sut -> {
            givenStep.run();
        };
        return this;
    }

    @Override
    public WhenDsl<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        this.givenStep = givenStep;
        return this;
    }

    @Override
    public <$Result> ThenDsl<$SystemUnderTest, $Result> when(Function<$SystemUnderTest, $Result> whenStep) {
        return toThenStep(whenStep);
    }

    @Override
    public ThenDsl<$SystemUnderTest, Void> when(Consumer<$SystemUnderTest> whenStep) {
        return toThenStep(sut -> {
            whenStep.accept(sut);
            return null;
        });
    }

    private <$Result> ThenDsl<$SystemUnderTest, $Result> toThenStep(Function<$SystemUnderTest, $Result> whenStep) {
        GivenWhenThenSteps<$SystemUnderTest, $Result> steps = new GivenWhenThenSteps<>(systemUnderTest);
        steps.setGivenStep(givenStep);
        steps.setWhenStep(whenStep);
        return new Then<>(steps);
    }
}
