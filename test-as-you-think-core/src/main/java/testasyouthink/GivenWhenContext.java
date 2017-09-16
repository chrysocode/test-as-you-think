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

import testasyouthink.execution.Execution;
import testasyouthink.preparation.Preparation;

import java.util.Optional;

public class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private final Execution<$SystemUnderTest, $Result> execution;
    private Optional<$Result> result;

    GivenWhenContext(Preparation<$SystemUnderTest> preparation, Execution<$SystemUnderTest, $Result> execution) {
        this.preparation = preparation;
        this.execution = execution;
    }

    public void prepareFixturesSeparately() {
        preparation.prepareFixturesSeparately();
    }

    public $Result returnResultOrVoid() {
        if (result == null) {
            preparation.prepareFixtures();
            result = execution.run();
        }
        return result.orElse(null);
    }

    public $SystemUnderTest getSystemUnderTest() {
        return preparation
                .supplySut()
                .get();
    }
}
