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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public abstract class OutputBuilder {

    List<TokenInfo> tokenList = new ArrayList<>();
    PrintStream out;

    public static class Factory {
        public static OutputBuilder create(Output output, PrintStream out) {
            OutputBuilder builder = null;
            switch (output) {
                case wakati:
                    builder = new WakatiOutputBuilder(out);
                    break;
                case mecab:
                    builder = new MeCabOutputBuilder(out);
                    break;
                case json:
                    builder = new JSONOutputBuilder(out);
                    break;
            }
            return builder;
        }
    }

    private OutputBuilder() {}

    public OutputBuilder(PrintStream out) {
        this.out = out;
    }

    public void addTerm(TokenInfo tokenInfo) {
        tokenList.add(tokenInfo);
    }

    public abstract void output();

}
