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

import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.Functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;

public class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final SutPreparation sutPreparation = SutPreparation.INSTANCE;
    private final ArgumentPreparation argumentPreparation = ArgumentPreparation.INSTANCE;
    private final Queue<Consumer<$SystemUnderTest>> givenSteps;
    private Supplier<$SystemUnderTest> givenSutStep;
    private Queue<Supplier> argumentSuppliers;
    private $SystemUnderTest systemUnderTest;
    private boolean standardStreamsCaptured;

    public Preparation() {
        givenSteps = new ArrayDeque<>();
        argumentSuppliers = new LinkedList<>();
        standardStreamsCaptured = false;
    }

    public Preparation(Class<$SystemUnderTest> sutClass) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(sutClass);
    }

    public Preparation($SystemUnderTest systemUnderTest) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(systemUnderTest);
    }

    public Preparation(CheckedSupplier<$SystemUnderTest> givenSutStep) {
        this();
        this.givenSutStep = sutPreparation.buildSutSupplier(givenSutStep);
    }

    public void recordGivenStep(CheckedRunnable givenStep) {
        givenSteps.add(functions.toConsumer(() -> {
            try {
                givenStep.run();
            } catch (Throwable throwable) {
                throw new PreparationError("Fails to prepare the test fixture!", throwable);
            }
        }));
    }

    public void recordGivenStep(CheckedConsumer<$SystemUnderTest> givenStep) {
        givenSteps.add(sutPreparation.buildSutSupplier(givenStep));
    }

    public <$Argument> void recordGivenStep(CheckedSupplier<$Argument> givenStep) {
        argumentSuppliers.add(argumentPreparation.buidArgumentSupplier(givenStep));
    }

    public <$Argument> void recordGivenStep(Class<$Argument> mutableArgumentClass,
            CheckedConsumer<$Argument> givenStep) {
        argumentSuppliers.add(argumentPreparation.buildMutableArgumentSupplier(mutableArgumentClass, givenStep));
    }

    public Queue<Supplier> getArgumentSuppliers() {
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

    public void prepareFixturesSeparately() {
        prepareFixtures();
        argumentSuppliers.forEach(Supplier::get);
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

    public void captureStandardStreamsSeparately() {
        if (!standardStreamsCaptured) {
            recordGivenStep(() -> {
                Path stdoutPath = Files.createTempFile("stdout_as_actual_result", ".txt");
                stdoutPath
                        .toFile()
                        .deleteOnExit();
                StandardRedirection.STDOUT_PATHS.put(currentThread().getId(), stdoutPath);
                StandardRedirection.STDOUT_STREAMS_TO_FILE.put(currentThread().getId(),
                        new PrintStream(stdoutPath.toString()));

                Path stderrPath = Files.createTempFile("stderr_as_actual_result", ".txt");
                stderrPath
                        .toFile()
                        .deleteOnExit();
                StandardRedirection.STDERR_PATHS.put(currentThread().getId(), stderrPath);
                StandardRedirection.STDERR_STREAMS_TO_FILE.put(currentThread().getId(),
                        new PrintStream(stderrPath.toString()));
            });

            standardStreamsCaptured = true;
        }
    }

    public Path getStdoutPath() {
        return StandardRedirection.STDOUT_PATHS.get(currentThread().getId());
    }

    public Path getStderrPath() {
        return StandardRedirection.STDERR_PATHS.get(currentThread().getId());
    }

    private static class StandardRedirection {

        private static final Map<Long, Path> STDOUT_PATHS;
        private static final Map<Long, Path> STDERR_PATHS;
        private static final Map<Long, PrintStream> STDOUT_STREAMS_TO_FILE;
        private static final Map<Long, PrintStream> STDERR_STREAMS_TO_FILE;
        private static final PrintStream SYSTEM_OUT;
        private static final PrintStream SYSTEM_ERR;

        static {
            SYSTEM_OUT = System.out;
            SYSTEM_ERR = System.err;
            STDOUT_PATHS = new HashMap<>();
            STDERR_PATHS = new HashMap<>();
            STDOUT_STREAMS_TO_FILE = new HashMap<>();
            STDERR_STREAMS_TO_FILE = new HashMap<>();

            commuteStandardStreams();
        }

        private static void commuteStandardStreams() {
            redirectStreamOnce(SYSTEM_OUT, System::setOut);
            redirectStreamOnce(SYSTEM_ERR, System::setErr);
        }

        private static void redirectStreamOnce(final PrintStream printStream, Consumer<PrintStream> redirectTo) {
            if (printStream == SYSTEM_OUT) {
                PrintStream allInOne = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        printStream.write(b);
                        if (!STDOUT_STREAMS_TO_FILE.isEmpty()) {
                            STDOUT_STREAMS_TO_FILE
                                    .get(currentThread().getId())
                                    .write(b);
                        }
                    }
                });
                redirectTo.accept(allInOne);
            } else if (printStream == SYSTEM_ERR) {
                PrintStream allInOne = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        printStream.write(b);
                        if (!STDERR_STREAMS_TO_FILE.isEmpty()) {
                            STDERR_STREAMS_TO_FILE
                                    .get(currentThread().getId())
                                    .write(b);
                        }
                    }
                });
                redirectTo.accept(allInOne);
            }
        }
    }
}
