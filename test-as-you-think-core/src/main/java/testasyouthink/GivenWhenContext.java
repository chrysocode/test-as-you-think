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

import testasyouthink.execution.Event;
import testasyouthink.preparation.Preparation;

class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private final Event<$SystemUnderTest, $Result> event;
    private $Result result;

    GivenWhenContext(Preparation<$SystemUnderTest> preparation, Event<$SystemUnderTest, $Result> event) {
        this.preparation = preparation;
        this.event = event;
    }

    $Result returnResultOrVoid() {
        if (result == null) {
            preparation.prepareFixtures();
            result = event.happen();
        }
        return result;
    }

    $SystemUnderTest getSystemUnderTest() {
        return preparation
                .supplySut()
                .get();
    }
}
