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

package testasyouthink.fixture;

import static org.easymock.EasyMock.*;

public class GivenWhenThenDefinition {

    private static final int DEFAULT_NUMBER_OF_STEPS = 1;

    public static GivenWhenThenDefinition orderedSteps() {
        return orderedSteps(DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps) {
        return orderedSteps(numberOfGivenSteps, DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps, int numberOfThenSteps) {
        return orderedSteps(numberOfGivenSteps, DEFAULT_NUMBER_OF_STEPS, numberOfThenSteps);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps, int numberOfWhenSteps,
            int numberOfThenSteps) {
        GivenWhenThenDefinition mock = strictMock(GivenWhenThenDefinition.class);

        if (numberOfGivenSteps > 0) {
            mock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            expectLastCall().times(numberOfGivenSteps);
        }
        if (numberOfWhenSteps > 0) {
            mock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            expectLastCall().times(numberOfWhenSteps);
        }
        if (numberOfThenSteps > 0) {
            mock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            expectLastCall().times(numberOfThenSteps);
        }

        replay(mock);
        return mock;
    }

    public void givenAContextThatDefinesTheInitialStateOfTheSystem() {}

    public void whenAnEventHappensInRelationToAnActionOfTheConsumer() {}

    public void thenTheActualResultIsInKeepingWithTheExpectedResult() {}
}
