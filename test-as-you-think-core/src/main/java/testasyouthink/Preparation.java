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

import testasyouthink.function.Functions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final Supplier<$SystemUnderTest> givenSutStep;
    private final List<Consumer<$SystemUnderTest>> givenSteps;
    private final Queue<Supplier> argumentSuppliers;
    private $SystemUnderTest systemUnderTest;

    Preparation(Supplier<$SystemUnderTest> givenSutStep) {
        this.givenSutStep = givenSutStep;
        givenSteps = new ArrayList<>();
        argumentSuppliers = new LinkedList<>();
    }

    void recordGivenStep(Runnable givenStep) {
        givenSteps.add(functions.toConsumer(givenStep));
    }

    void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    <$Argument> void recordGivenStep(Supplier<$Argument> givenStep) {
        argumentSuppliers.add(givenStep);
    }

    Queue<Supplier> getArgumentSuppliers() {
        return argumentSuppliers;
    }

    void prepareFixtures() {
        givenSteps.forEach(step -> step.accept(systemUnderTest()));
    }

    private $SystemUnderTest systemUnderTest() {
        if (systemUnderTest == null && givenSutStep != null) {
            systemUnderTest = givenSutStep.get();
        }
        return systemUnderTest;
    }

    Supplier<$SystemUnderTest> supplySut() {
        return () -> systemUnderTest();
    }
}
