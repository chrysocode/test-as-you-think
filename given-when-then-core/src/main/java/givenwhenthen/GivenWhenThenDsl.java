package givenwhenthen;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface GivenWhenThenDsl {

    interface Given<$SystemUnderTest> extends AndGiven<$SystemUnderTest>, When<$SystemUnderTest> {

        When<$SystemUnderTest> given(Runnable givenStep);

        Given<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep);

        Given<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep);

        When<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);
    }

    interface AndGiven<$SystemUnderTest> extends When<$SystemUnderTest> {

        AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep);

        AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);

        <$Input> WhenApplyingInput<$SystemUnderTest, $Input> andInput(Supplier<$Input> givenStep);
    }

    interface WhenApplyingInput<$SystemUnderTest, $Input> {

        ThenWithoutResult<$SystemUnderTest> when(BiConsumer<$SystemUnderTest, $Input> whenStep);
    }

    interface When<$SystemUnderTest> {

        <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep);

        ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep);

        ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep);
    }

    interface Then<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> then(Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> then(Predicate<$Result> thenStep);

        void then(List<Predicate<$Result>> thenSteps);

        void then(BiConsumer<$SystemUnderTest, $Result> thenStep);

        void then(BiPredicate<$SystemUnderTest, $Result> thenStep);

        void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest);
    }

    interface AndThen<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> and(Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> and(Predicate<$Result> thenStep);
    }

    interface ThenWithoutResult<$SystemUnderTest> {

        AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(Consumer<$SystemUnderTest> thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Consumer<$SystemUnderTest>
                thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(BooleanSupplier thenStep);
    }

    interface AndThenWithoutResult<$SystemUnderTest> {

        AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Consumer<$SystemUnderTest>
                thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(BooleanSupplier thenStep);
    }

    interface ThenFailure {

        void thenItFails();

        void thenItFails(Class<? extends Throwable> expectedThrowableClass);

        void thenItFails(Class<? extends Throwable> expectedThrowableClass, String expectedMessage);
    }
}