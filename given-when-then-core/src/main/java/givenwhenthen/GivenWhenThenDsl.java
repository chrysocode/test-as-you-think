package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

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

        <$Result> Then<$SystemUnderTest, $Result> when(Function<$SystemUnderTest, $Result> whenStep);

        Then<$SystemUnderTest, Void> when(Consumer<$SystemUnderTest> whenStep);
    }

    public static interface Then<$SystemUnderTest, $Result> {

        void then(Consumer<$Result> thenStep);

        void then(Runnable thenStep);

        void then(String expectationSpecification, Consumer<$Result> thenStep);
    }
}