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

package info.johtani.misc.cli.kuromoji.tokanizer;

import com.atilika.kuromoji.TokenizerBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TokenizerFactoryTest {

    @Test
    public void createIpadic() {
        Object obj = TokenizerFactory.create(DictionaryType.ipadic, TokenizerBase.Mode.NORMAL);
        assertInstanceOf(com.atilika.kuromoji.ipadic.Tokenizer.class, obj);
    }

    @Test
    public void createUnidic() {
        Object obj = TokenizerFactory.create(DictionaryType.unidic, TokenizerBase.Mode.NORMAL);
        assertInstanceOf(com.atilika.kuromoji.unidic.Tokenizer.class, obj);
    }

    @Test
    public void createUnidicKanaaccent() {
        Object obj = TokenizerFactory.create(DictionaryType.unidic_kanaaccent, TokenizerBase.Mode.NORMAL);
        assertInstanceOf(com.atilika.kuromoji.unidic.kanaaccent.Tokenizer.class, obj);
    }

    @Test
    public void createJumandic() {
        Object obj = TokenizerFactory.create(DictionaryType.jumandic, TokenizerBase.Mode.NORMAL);
        assertInstanceOf(com.atilika.kuromoji.jumandic.Tokenizer.class, obj);
    }

    @Test
    public void createNaistjdic() {
        Object obj = TokenizerFactory.create(DictionaryType.naist_jdic, TokenizerBase.Mode.NORMAL);
        assertInstanceOf(com.atilika.kuromoji.naist.jdic.Tokenizer.class, obj);
    }
}
