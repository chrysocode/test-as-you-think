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

public class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final SutPreparation sutPreparation = SutPreparation.INSTANCE;
    private final ArgumentPreparation argumentPreparation = ArgumentPreparation.INSTANCE;
    private final Queue<Consumer<$SystemUnderTest>> givenSteps;
    private Supplier<$SystemUnderTest> givenSutStep;
    private Queue<Supplier> argumentSuppliers;
    private $SystemUnderTest systemUnderTest;
    private Path stdoutPath;

    public Preparation() {
        givenSteps = new ArrayDeque<>();
        argumentSuppliers = new LinkedList<>();
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

    public void captureStdout() {
        recordGivenStep(() -> {
            stdoutPath = Files.createTempFile("actual_result", ".txt");
            stdoutPath
                    .toFile()
                    .deleteOnExit();
            StdoutRedirection.STDOUT_STREAMS.put(Thread
                    .currentThread()
                    .getId(), new PrintStream(stdoutPath.toString()));
        });
    }

    public Path getStdoutPath() {
        return stdoutPath;
    }

    private static class StdoutRedirection {

        private static final Map<Long, PrintStream> STDOUT_STREAMS;
        private static final PrintStream SYSTEM_OUT;
        private static final PrintStream SYSTEM_ERR;

        static {
            SYSTEM_OUT = System.out;
            SYSTEM_ERR = System.err;
            STDOUT_STREAMS = new HashMap<>();
            redirectOutputOnce(SYSTEM_OUT, System::setOut);
            redirectOutputOnce(SYSTEM_ERR, System::setErr);
        }

        private static void redirectOutputOnce(final PrintStream printStream, Consumer<PrintStream> redirectTo) {
            PrintStream allInOne = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    printStream.write(b);
                    if (!STDOUT_STREAMS.isEmpty()) {
                        STDOUT_STREAMS
                                .get(Thread
                                        .currentThread()
                                        .getId())
                                .write(b);
                    }
                }
            });
            redirectTo.accept(allInOne);
        }
    }
}
