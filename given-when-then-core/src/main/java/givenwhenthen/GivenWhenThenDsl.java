package givenwhenthen;

import java.util.function.Consumer;
import java.util.function.Function;

public interface GivenWhenThenDsl {

    public static interface GivenDsl<$SystemUnderTest> {

        WhenDsl<$SystemUnderTest> given(Runnable givenStep);

        WhenDsl<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep);
    }

    public static interface WhenDsl<$SystemUnderTest> {

        <$Result> ThenDsl<$SystemUnderTest, $Result> when(Function<$SystemUnderTest, $Result> whenStep);

        ThenDsl<$SystemUnderTest, Void> when(Consumer<$SystemUnderTest> whenStep);
    }

    public static interface ThenDsl<$SystemUnderTest, $Result> {

        void then(Consumer<$Result> thenStep);

        void then(Runnable thenStep);
    }
}