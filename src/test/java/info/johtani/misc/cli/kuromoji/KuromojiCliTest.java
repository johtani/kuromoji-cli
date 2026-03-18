/*
 * Copyright (c) 2020 johtani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.johtani.misc.cli.kuromoji;

import com.atilika.kuromoji.TokenizerBase;
import info.johtani.misc.cli.kuromoji.output.Output;
import info.johtani.misc.cli.kuromoji.tokenizer.DictionaryType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KuromojiCliTest {

    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final java.io.InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(errContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    String getDefaultString() {
        return "早稲田大学";
    }

    @Test
    public void tokenize() {
        KuromojiCli target = new KuromojiCli();
        target.tokenize(getDefaultString(), Output.wakati, DictionaryType.ipadic, TokenizerBase.Mode.EXTENDED);
        String output = outContent.toString(StandardCharsets.UTF_8).trim();
        assertFalse(output.isEmpty());

        Set<String> tokens = Stream.of(output.split("\\s+"))
                .filter(token -> token.isEmpty() == false)
                .collect(Collectors.toSet());

        // Accept minor tokenizer variations across dictionary/library versions.
        boolean containsCompound = tokens.contains("早稲田大学");
        boolean containsSplit = tokens.contains("早稲田") && tokens.contains("大学");
        assertTrue(containsCompound || containsSplit);
    }

    @Test
    public void callWithInputFileShouldIgnoreStdin() throws IOException {
        Path inputPath = Files.createTempFile("kuromoji-cli-", ".txt");
        try {
            Files.writeString(inputPath, "FILEMARKER\n", StandardCharsets.UTF_8);
            System.setIn(new ByteArrayInputStream("STDINMARKER\n".getBytes(StandardCharsets.UTF_8)));

            KuromojiCli target = new KuromojiCli();
            target.inputFile = inputPath.toString();
            target.output = Output.wakati;
            target.dictType = DictionaryType.ipadic;
            target.mode = TokenizerBase.Mode.EXTENDED;

            int exitCode = target.call();
            String output = outContent.toString(StandardCharsets.UTF_8).trim();

            assertEquals(0, exitCode);
            assertFalse(output.isEmpty());
            assertFalse(output.contains("STDINMARKER"));
        } finally {
            Files.deleteIfExists(inputPath);
        }
    }
}
