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

package testasyouthink.preparation;

import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.Functions;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final SutPreparation sutPreparation = SutPreparation.INSTANCE;
    private final ArgumentPreparation argumentPreparation = ArgumentPreparation.INSTANCE;
    private final Queue<Consumer<$SystemUnderTest>> givenSteps;
    private Supplier<$SystemUnderTest> givenSutStep;
    private Queue<CheckedSupplier> argumentSuppliers;
    private $SystemUnderTest systemUnderTest;

    public Preparation() {
        givenSteps = new ArrayDeque<>();
        argumentSuppliers = new LinkedList<>();
    }

    public Preparation(Class<$SystemUnderTest> sutClass) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(sutClass);
    }

    public Preparation($SystemUnderTest systemUnderTest) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(systemUnderTest);
    }

    public Preparation(Supplier<$SystemUnderTest> givenSutStep) {
        this();
        this.givenSutStep = givenSutStep;
    }

    public void recordGivenStep(Runnable givenStep) {
        givenSteps.add(functions.toConsumer(givenStep));
    }

    public void recordGivenStep(Consumer<$SystemUnderTest> givenStep) {
        givenSteps.add(givenStep);
    }

    public <$Argument> void recordGivenStep(CheckedSupplier<$Argument> givenStep) {
        argumentSuppliers.add(givenStep);
    }

    public <$Argument> void recordGivenStep(Class<$Argument> mutableArgumentClass, Consumer<$Argument> givenStep) {
        argumentSuppliers.add(argumentPreparation.buildMutableArgumentSupplier(mutableArgumentClass, givenStep));
    }

    public Queue<CheckedSupplier> getArgumentSuppliers() {
        return argumentSuppliers;
    }

    public void prepareFixtures() {
        $SystemUnderTest sutToPrepareAtFirst = systemUnderTest();
        while (!givenSteps.isEmpty()) {
            givenSteps
                    .poll()
                    .accept(sutToPrepareAtFirst);
        }
    }

    private $SystemUnderTest systemUnderTest() {
        if (systemUnderTest == null && givenSutStep != null) {
            systemUnderTest = givenSutStep.get();
        }
        return systemUnderTest;
    }

    public Supplier<$SystemUnderTest> supplySut() {
        return this::systemUnderTest;
    }
}
