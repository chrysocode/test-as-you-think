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

package testasyouthink.execution;

import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.Functions;
import testasyouthink.preparation.PreparationError;

import java.util.function.Supplier;

public class Event<$SystemUnderTest, $Result> {

    public static final String EXECUTION_FAILURE_MESSAGE = "Fails to execute the target method " //
            + "of the system under test because of an unexpected failure!";
    private final Functions functions = Functions.INSTANCE;
    private final Supplier<$SystemUnderTest> givenSutStep;
    private final CheckedFunction<$SystemUnderTest, $Result> whenStep;

    public Event(Supplier<$SystemUnderTest> givenSutStep, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.givenSutStep = givenSutStep;
        this.whenStep = whenStep;
    }

    public Event(Supplier<$SystemUnderTest> givenSutStep, CheckedConsumer<$SystemUnderTest> whenStep) {
        this.givenSutStep = givenSutStep;
        this.whenStep = functions.toFunction(whenStep);
    }

    public $Result happen() {
        $Result result;
        try {
            result = whenStep.apply(givenSutStep.get());
        } catch (PreparationError preparationError) {
            throw preparationError;
        } catch (Throwable throwable) {
            throw new ExecutionError(EXECUTION_FAILURE_MESSAGE, throwable);
        }

        return result;
    }
}
