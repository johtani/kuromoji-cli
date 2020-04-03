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
import info.johtani.misc.cli.kuromoji.tokanizer.DictionaryType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class KuromojiCliTest {

    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    String getDefaultString() {
        return "自転車を漕ぐ";
    }

    String getExpectedTokens() {
        return "自転車 を 漕ぐ\n";
    }

    @Test
    public void tokenize() {
        KuromojiCli target = new KuromojiCli();
        target.tokenize(getDefaultString(), Output.wakati, DictionaryType.ipadic, TokenizerBase.Mode.NORMAL);
        assertEquals(getExpectedTokens(), outContent.toString());
    }
}