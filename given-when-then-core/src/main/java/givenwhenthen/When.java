package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

public class When<S> {

    private Given<S> given;

    When(Given<S> given) {
        this.given = given;
    }

    public <R> Then<S, R> when(Function<S, R> whenStep) {
        GivenWhenThenSteps<S, R> steps = new GivenWhenThenSteps<>(given.getSystemUnderTest());
        steps.setGivenStep(given.getGivenStep());
        steps.setWhenStep(whenStep);
        return new Then<>(steps);
    }

    public Then<S, Void> when(Consumer<S> whenStep) {
        GivenWhenThenSteps<S, Void> steps = new GivenWhenThenSteps<>(given.getSystemUnderTest());
        steps.setGivenStep(given.getGivenStep());
        steps.setWhenStep((sut) -> {
            whenStep.accept(sut);
            return null;
        });
        return new Then<>(steps);
    }
}
