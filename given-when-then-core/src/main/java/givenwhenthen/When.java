package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

public class When<$SystemUnderTest> {

    private Given<$SystemUnderTest> given;

    When(Given<$SystemUnderTest> given) {
        this.given = given;
    }

    public <R> Then<$SystemUnderTest, R> when(Function<$SystemUnderTest, R> whenStep) {
        GivenWhenThenSteps<$SystemUnderTest, R> steps = new GivenWhenThenSteps<>(given.getSystemUnderTest());
        steps.setGivenStep(given.getGivenStep());
        steps.setWhenStep(whenStep);
        return new Then<>(steps);
    }

    public Then<$SystemUnderTest, Void> when(Consumer<$SystemUnderTest> whenStep) {
        GivenWhenThenSteps<$SystemUnderTest, Void> steps = new GivenWhenThenSteps<>(given.getSystemUnderTest());
        steps.setGivenStep(given.getGivenStep());
        steps.setWhenStep((sut) -> {
            whenStep.accept(sut);
            return null;
        });
        return new Then<>(steps);
    }
}
