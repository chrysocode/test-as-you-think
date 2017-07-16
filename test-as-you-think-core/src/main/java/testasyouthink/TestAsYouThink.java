/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 Xavier Pigeon and TestAsYouThink contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package testasyouthink;

import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGiven;
import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.Functions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestAsYouThink {

    private static Functions functions = Functions.INSTANCE;
    private static ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;

    private TestAsYouThink() {}

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new GivenWhenSteps<>(() -> systemUnderTest);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut(Supplier<$SystemUnderTest> givenStep) {
        return new GivenWhenSteps<>(givenStep);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass) {
        return new GivenWhenSteps<>(() -> {
            $SystemUnderTest sut;
            try {
                sut = sutClass.newInstance();
            } catch (Exception exception) {
                throw new PreparationError("Fails to instantiate the system under test!", exception);
            }
            return sut;
        });
    }

    public static <$SystemUnderTest> AndGiven<$SystemUnderTest> givenSut(Class<$SystemUnderTest> sutClass,
            Consumer<$SystemUnderTest> givenStep) {
        return givenSutClass(sutClass).given(givenStep);
    }

    public static ThenWithoutResultStep<Void> when(Runnable whenStep) {
        return thenStepFactory.createThenStep(functions.toCheckedConsumer(whenStep));
    }

    public static <$Result> Then<Void, $Result> when(Supplier<$Result> whenStep) {
        return thenStepFactory.createThenStep(functions.toCheckedFunction(whenStep));
    }

    public static <$SystemUnderTest, $Result> CheckedFunction<$SystemUnderTest, $Result> withReturn(
            CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return whenStep;
    }
}
