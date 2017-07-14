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

import org.hibernate.annotations.Immutable;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGiven;
import testasyouthink.GivenWhenThenDsl.PreparationStage.AndGivenArgument;
import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.Functions;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class GivenWhenSteps<$SystemUnderTest> implements Given<$SystemUnderTest>, AndGiven<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final Preparation<$SystemUnderTest> preparation;
    private final ThenStepFactory thenStepFactory = ThenStepFactory.INSTANCE;

    GivenWhenSteps(Supplier<$SystemUnderTest> givenSutStep) {
        preparation = new Preparation<>(givenSutStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> given(Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, Runnable givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> given(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        preparation.recordGivenStep(givenStep);
        return this;
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Runnable givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    @Override
    public AndGiven<$SystemUnderTest> and(String fixtureSpecification, Consumer<$SystemUnderTest> givenStep) {
        return given(fixtureSpecification, givenStep);
    }

    private <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> prepareArgument(Supplier<$Argument> givenStep) {
        preparation.recordGivenStep(givenStep);
        return new GivenArgumentWhenSteps<>(preparation);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(Supplier<$Argument> givenStep) {
        return prepareArgument(givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
            Class<$Argument> immutableArgumentClass, Function<$Argument, $Argument> givenStep) {
        return prepareArgument(() -> {
            $Argument argument = null;
            try {
                if (asList(BigDecimal.class, BigInteger.class, Boolean.class, Byte.class, Character.class, Double.class,
                        File.class, Float.class, Integer.class, Long.class, Short.class, String.class)
                        .stream()
                        .anyMatch(immutableArgumentClass::equals)) {
                    argument = givenStep.apply(argument);
                } else if (immutableArgumentClass.isAnnotationPresent(Immutable.class)) {
                    Constructor<$Argument> argumentConstructor = immutableArgumentClass.getConstructor(
                            immutableArgumentClass);
                    argument = argumentConstructor.newInstance(givenStep.apply(argument));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new RuntimeException("Not yet implemented!");
            }
            return argument;
        });
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(
            Class<$Argument> mutableArgumentClass, Consumer<$Argument> givenStep) {
        return prepareArgument(() -> {
            $Argument argument;
            try {
                argument = mutableArgumentClass.newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new RuntimeException("Not yet implemented!");
            }
            givenStep.accept(argument);
            return argument;
        });
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            Supplier<$Argument> givenStep) {
        return givenArgument(givenStep);
    }

    @Override
    public <$Argument> AndGivenArgument<$SystemUnderTest, $Argument> givenArgument(String description,
            $Argument argument) {
        return prepareArgument(functions.toSupplier(argument));
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> when(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public <$Result> Then<$SystemUnderTest, $Result> whenSutReturns(
            CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        return when(whenStep);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> when(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, whenStep);
    }

    @Override
    public ThenWithoutResult<$SystemUnderTest> whenSutRuns(CheckedConsumer<$SystemUnderTest> whenStep) {
        return when(whenStep);
    }

    @Override
    public ThenFailure whenSutRunsOutsideOperatingConditions(CheckedConsumer<$SystemUnderTest> whenStep) {
        return thenStepFactory.createThenStep(preparation, functions.toFunctionWithThrowableAsResult(whenStep));
    }
}
