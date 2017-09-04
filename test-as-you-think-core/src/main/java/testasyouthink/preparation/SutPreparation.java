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

import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedSupplier;

import java.util.function.Consumer;
import java.util.function.Supplier;

enum SutPreparation {

    INSTANCE;

    <$SystemUnderTest> Supplier<$SystemUnderTest> buildSutSupplier($SystemUnderTest systemUnderTest) {
        return () -> systemUnderTest;
    }

    <$SystemUnderTest> Supplier<$SystemUnderTest> buildSutSupplier(Class<$SystemUnderTest> sutClass) {
        return () -> {
            $SystemUnderTest sut;
            try {
                sut = sutClass.newInstance();
            } catch (Exception exception) {
                throw new PreparationError("Fails to instantiate the system under test!", exception);
            }
            return sut;
        };
    }

    <$SystemUnderTest> Supplier<$SystemUnderTest> buildSutSupplier(CheckedSupplier<$SystemUnderTest> sutSupplier) {
        return () -> {
            try {
                return sutSupplier.get();
            } catch (Throwable throwable) {
                throw new PreparationError("Fails to prepare the system under test!", throwable);
            }
        };
    }

    public <$SystemUnderTest> Consumer<$SystemUnderTest> buildSutSupplier(CheckedConsumer<$SystemUnderTest> givenStep) {
        return sut -> {
            try {
                givenStep.accept(sut);
            } catch (Throwable throwable) {
                throw new PreparationError("Fails to prepare the system under test!", throwable);
            }
        };
    }
}
