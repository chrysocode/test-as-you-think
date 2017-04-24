package givenwhenthen;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
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

        Then<$SystemUnderTest, Void> when(CheckedConsumer<$SystemUnderTest> whenStep);

        ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep);
    }

    public static interface Then<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep);

        void then(String expectationSpecification, Consumer<$Result> thenStep);

        void then(Runnable thenStep);

        void then(String expectationSpecification, Runnable thenStep);

        void then(Predicate<$Result> thenStep);

        void then(List<Predicate<$Result>> thenSteps);

        void then(BiConsumer<$SystemUnderTest, $Result> thenStep);

        void then(BiPredicate<$SystemUnderTest, $Result> thenStep);

        void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest);
    }

    public static interface ThenFailure {

        void thenItFails();

        void thenItFails(Class<? extends Throwable> expectedThrowableClass);

        void thenItFails(Class<? extends Throwable> expectedThrowableClass, String expectedMessage);
    }

    public static interface AndThen<$SystemUnderTest, $Result> {

        AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep);

    }
}