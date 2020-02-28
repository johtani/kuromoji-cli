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

package info.johtani.misc.cli.kuromoji.output;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MeCabOutputBuilderTest extends AbstractOutputBuilderTest {

    static MeCabOutputBuilder createInstance() {
        return new MeCabOutputBuilder();
    }



    @Test
    public void output() {
        OutputBuilder builder = createInstance();
        TokenInfo token = defaultTokenInfo();
        builder.addTerm(token);
        builder.addTerm(token);
        builder.output();
        assertEquals(token.getToken() + "\t" +
                        token.getAllFeatures() + "\n" +
                        token.getToken() + "\t" +
                        token.getAllFeatures() + "\n" +
                        "EOS\n",
                outContent.toString());
    }
}