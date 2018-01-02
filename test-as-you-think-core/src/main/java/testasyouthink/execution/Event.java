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

import testasyouthink.function.CheckedFunction;

import java.util.function.Supplier;

class Event<$SystemUnderTest, $Result> {

    private final Supplier<$SystemUnderTest> givenSutStep;
    private final CheckedFunction<$SystemUnderTest, $Result> whenStep;

    Event(Supplier<$SystemUnderTest> givenSutStep, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.givenSutStep = givenSutStep;
        this.whenStep = whenStep;
    }

    $Result happen() throws Throwable {
        return whenStep.apply(givenSutStep.get());
    }
}
