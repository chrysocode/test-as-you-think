package givenwhenthen;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GivenWhenThenDsl {

    public static interface Given<$SystemUnderTest> extends AndGiven<$SystemUnderTest>, When<$SystemUnderTest> {

        When<$SystemUnderTest> given(Runnable givenStep);

        When<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep);

        Given<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep);

        When<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);
    }

    public static interface AndGiven<$SystemUnderTest> extends When<$SystemUnderTest> {

        AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep);

        AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep);
    }

    public static interface When<$SystemUnderTest> {

        <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep);

        ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep);

        ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep);
    }

    public static interface Then<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> then(Runnable thenStep);

        void then(String expectationSpecification, Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> then(Predicate<$Result> thenStep);

        void then(List<Predicate<$Result>> thenSteps);

        void then(BiConsumer<$SystemUnderTest, $Result> thenStep);

        void then(BiPredicate<$SystemUnderTest, $Result> thenStep);

        void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest);
    }

    public static interface AndThen<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep);

        AndThen<$SystemUnderTest, $Result> and(Runnable thenStep);

        AndThen<$SystemUnderTest, $Result> and(Predicate<$Result> thenStep);
    }

    public static interface ThenWithoutResult<$SystemUnderTest> {

        AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> then(Consumer<$SystemUnderTest> thenStep);

        void then(BooleanSupplier thenStep);
    }

    public static interface AndThenWithoutResult<$SystemUnderTest> {

        AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Runnable thenStep);

        AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep);
    }

    public static interface ThenFailure {

        void thenItFails();

        void thenItFails(Class<? extends Throwable> expectedThrowableClass);

        void thenItFails(Class<? extends Throwable> expectedThrowableClass, String expectedMessage);
    }

}