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

public class TokenInfo {
    String token;
    String pos;
    String reading;
    String pronunciation;
    String baseForm;
    String inflectionType;
    String inflectionForm;

    public TokenInfo(String token, String pos, String reading, String pronunciation, String baseForm,
                     String inflectionType, String inflectionForm) {
        this.token = token;
        this.pos = pos;
        this.reading = reading;
        this.pronunciation = pronunciation;
        this.baseForm = baseForm;
        this.inflectionType = inflectionType;
        this.inflectionForm = inflectionForm;
    }

    public String getToken() {
        return token;
    }

    public String getPos() {
        return pos;
    }

    public String getReading() {
        return reading;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getBaseForm() {
        return baseForm;
    }

    public String getInflectionType() {
        return inflectionType;
    }

    public String getInflectionForm() {
        return inflectionForm;
    }
}
