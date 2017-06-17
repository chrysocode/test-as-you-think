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

public class SystemUnderTest {

    private GivenWhenThenDefinition givenWhenThenDefinition;
    private String state;

    public SystemUnderTest() {}

    public SystemUnderTest(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    private void whenAnEventHappens() {
        if (givenWhenThenDefinition != null) {
            givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        }
    }

    public String nonVoidMethod() {
        whenAnEventHappens();
        changeState();
        return "expected result";
    }

    public void voidMethod() {
        whenAnEventHappens();
        changeState();
    }

    public void voidMethodWithParameter(String parameter) {
        voidMethod();
    }

    public String nonVoidMethodWithParameter(String parameter) {
        return nonVoidMethod();
    }

    public void voidMethodWithTwoParameters(String parameter1, Integer parameter2) {
        voidMethod();
    }

    public String nonVoidMethodWithTwoParameters(String parameter1, Integer parameter2) {
        return nonVoidMethod();
    }

    public void voidMethodWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) {
        voidMethod();
    }

    public String nonVoidMethodWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) {
        return nonVoidMethod();
    }

    public void failWithParameter(String parameter) throws Throwable {}

    public void failWithTwoParameters(String parameter1, Integer parameter2) throws Throwable {}

    public void failWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) throws Throwable {}

    public String nonVoidFailWithParameter(String parameter) throws Throwable {
        return null;
    }

    public String nonVoidFailWithTwoParameters(String parameter1, Integer parameter2) throws Throwable {
        return null;
    }

    public String nonVoidFailWithThreeParameters(String parameter1, Integer parameter2,
            Boolean parameter3) throws Throwable {
        return null;
    }

    public void voidMethodWithThrowsClause() throws Throwable {}

    public String nonVoidMethodWithThrowsClause() throws Throwable {
        return null;
    }

    public String methodWithThrowsClause() throws Throwable {
        return null;
    }

    private void changeState() {
        state = "state updated";
    }

    public void setGivenWhenThenDefinition(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    public String getState() {
        return state;
    }
}
