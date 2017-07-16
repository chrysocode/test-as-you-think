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

import java.util.function.Consumer;

enum ArgumentPreparation {

    INSTANCE;

    <$Argument> CheckedSupplier<$Argument> buildMutableArgumentSupplier(Class<$Argument> mutableArgumentClass,
            Consumer<$Argument> givenStep) {
        return () -> {
            $Argument argument;
            try {
                argument = mutableArgumentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new PreparationError("Fails to instantiate the argument of the " //
                        + mutableArgumentClass.getName() + " type!", exception);
            }
            givenStep.accept(argument);
            return argument;
        };
    }
}
