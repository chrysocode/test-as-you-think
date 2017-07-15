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
import testasyouthink.function.CheckedFunction;
import testasyouthink.function.CheckedSupplier;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

enum ArgumentPreparation {

    INSTANCE;

    <$Argument> CheckedSupplier<$Argument> buildMutableArgumentSupplier(Class<$Argument> mutableArgumentClass,
            Consumer<$Argument> givenStep) {
        return () -> {
            $Argument argument;
            try {
                argument = mutableArgumentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new RuntimeException("Impossible to instantiate the argument of the " //
                        + mutableArgumentClass.getName() + " type!", exception);
            }
            givenStep.accept(argument);
            return argument;
        };
    }

    public <$Argument> CheckedSupplier<$Argument> buildImmutableArgumentSupplier(
            Class<$Argument> immutableArgumentClass, CheckedFunction<$Argument, $Argument> givenStep) {
        return () -> {
            $Argument argument = null;
            if (asList(BigDecimal.class, BigInteger.class, Boolean.class, Byte.class, Character.class, Double.class,
                    File.class, Float.class, Integer.class, Long.class, Short.class, String.class)
                    .stream()
                    .anyMatch(immutableArgumentClass::equals)) {
                argument = givenStep.apply(argument);
            } else if (immutableArgumentClass.isAnnotationPresent(Immutable.class)) {
                Constructor<$Argument> argumentConstructor;
                try {
                    argumentConstructor = immutableArgumentClass.getConstructor(immutableArgumentClass);
                    argument = argumentConstructor.newInstance(givenStep.apply(argument));
                } catch (NoSuchMethodException exception) {
                    throw new RuntimeException("Impossible to instantiate the argument of the " //
                            + immutableArgumentClass.getName() + " type!" //
                            + " A copy constructor is missing.", exception);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException("Impossible to instantiate the argument of the " //
                            + immutableArgumentClass.getName() + " type!", exception);
                }
            }
            return argument;
        };
    }
}
