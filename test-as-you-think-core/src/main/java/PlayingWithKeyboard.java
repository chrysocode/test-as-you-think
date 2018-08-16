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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.rangeClosed;

public class PlayingWithKeyboard {

    public static void main(String[] args) {
        String message = "expected message";
        String givenMessages = rangeClosed(0, 9)
                .mapToObj(count -> String.format("%s #%d", message, count))
                .collect(joining("\n"));
        InputStream stdinFake = new ByteArrayInputStream(givenMessages.getBytes());
        System.setIn(stdinFake);

        try (BufferedReader actualStdin = new BufferedReader(new InputStreamReader(System.in))) {
            while (actualStdin.ready()) {
                System.out.print("Type: ");
                String actualInput = actualStdin.readLine();
                System.out.println(actualInput);
                if ("q".equals(actualInput)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
