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

import static com.atilika.kuromoji.TokenizerBase.Mode;

public interface TokenizerFactory {
    static TokenizerBase create(DictionaryType dictType, Mode mode) {
        TokenizerBase tokenizer = null;
        switch (dictType) {
            case ipadic:
                tokenizer = new com.atilika.kuromoji.ipadic.Tokenizer.Builder().mode(mode).build();
                break;
            case unidic:
                tokenizer = new com.atilika.kuromoji.unidic.Tokenizer.Builder().build();
                break;
            case jumandic:
                tokenizer = new com.atilika.kuromoji.jumandic.Tokenizer.Builder().build();
                break;
            case naist_jdic:
                tokenizer = new com.atilika.kuromoji.naist.jdic.Tokenizer.Builder().build();
                break;
            case unidic_kanaaccent:
                tokenizer = new com.atilika.kuromoji.unidic.kanaaccent.Tokenizer.Builder().build();
                break;
        }
        return tokenizer;
    }
}
