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

package testasyouthink.preparation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testasyouthink.GivenWhenThenDsl.Fixture.Stdin;
import testasyouthink.function.CheckedConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class StdinPreparationTest {

    private StdinPreparation sut;

    @BeforeEach
    void prepareFixture() {
        sut = new StdinPreparation();
    }

    @Nested
    class When_using_a_buffered_reader_to_read_stdin {

        @Test
        void should_fix_empty_stdin_while_a_collection_of_strings_is_expected() throws IOException {
            // GIVEN
            CheckedConsumer<Stdin> givenStep = stdin -> stdin.expectToRead(asList("input #1", "input #2", "input #3"));

            // WHEN
            sut
                    .buildStdin(givenStep)
                    .accept(null);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Collection<String> result = new ArrayList<>();
            while (reader.ready()) {
                result.add(reader.readLine());
            }
            reader.close();

            // THEN
            assertThat(result).isEmpty();
            fail("Bug repeated!");
        }
    }

    @Nested
    class When_using_a_scanner_to_read_stdin {

        @Test
        void should_fix_empty_stdin_while_a_collection_of_strings_is_expected() {
            // GIVEN
            CheckedConsumer<Stdin> givenStep = stdin -> stdin.expectToRead(asList("input #1", "input #2", "input #3"));

            // WHEN
            sut
                    .buildStdin(givenStep)
                    .accept(null);
            Scanner scanner = new Scanner(System.in);
            Collection<String> result = new ArrayList<>();
            while (scanner.hasNext()) {
                result.add(scanner.nextLine());
            }
            scanner.close();

            // THEN
            assertThat(result).containsExactly("input #1", "input #2", "input #3");
        }
    }
}
