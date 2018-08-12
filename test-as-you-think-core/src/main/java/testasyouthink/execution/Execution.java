/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 - 2018 Xavier Pigeon and TestAsYouThink contributors
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
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.CheckedSuppliers.CheckedArraySupplier;
import testasyouthink.function.Functions;
import testasyouthink.preparation.PreparationError;

import java.util.function.Supplier;

public class Execution<$SystemUnderTest, $Result> {

    public static final String EXECUTION_FAILURE_MESSAGE = "Fails to execute the target method " //
            + "of the system under test because of an unexpected failure!";
    private static final Functions FUNCTIONS = Functions.INSTANCE;
    private final Event<$SystemUnderTest, $Result> event;

    public Execution(Supplier<$SystemUnderTest> givenSutStep, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        event = new Event<>(givenSutStep, whenStep);
    }

    public Execution(Supplier<$SystemUnderTest> givenSutStep, CheckedConsumer<$SystemUnderTest> whenStep) {
        event = new Event<>(givenSutStep, FUNCTIONS.toFunction(whenStep));
    }

    public static <$Result> Execution<Void, $Result> of(CheckedSupplier<$Result> whenStep) {
        return new Execution<>(noExplicitSut(), FUNCTIONS.toCheckedFunction(whenStep));
    }

    public static <$Element> Execution<Void, $Element[]> of(CheckedArraySupplier<$Element> whenStep) {
        return new Execution<>(noExplicitSut(), FUNCTIONS.toCheckedFunction(whenStep));
    }

    private static Supplier<Void> noExplicitSut() {
        return () -> null;
    }

    public $Result run() {
        try {
            return event.happen();
        } catch (PreparationError preparationError) {
            throw preparationError;
        } catch (Throwable throwable) {
            throw new ExecutionError(EXECUTION_FAILURE_MESSAGE, throwable);
        }
    }
}
